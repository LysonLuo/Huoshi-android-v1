package im.huoshi.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.City;
import im.huoshi.utils.AmapUtils;
import im.huoshi.utils.JsonUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lyson on 16/2/5.
 */
public class AreaChooseActivity extends BaseActivity {
    @ViewInject(R.id.textview_current_area)
    private TextView mCurrentAreaTextView;
    @ViewInject(R.id.textview_province)
    private TextView mProvinceTextView;
    @ViewInject(R.id.textview_city)
    private TextView mCityTextView;
    @ViewInject(R.id.listview)
    private ListView mListView;
    private String mProvince;
    private String mCity;
    private String mProvinceId;
    private String mCityId;
    private AreaAdapter mAdapter;

    private List<City> mProvinceList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();
    private HashMap<String, List<City>> mCityMap = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_choose);
        ViewUtils.inject(this);

        getAddressInfo();
        getLocation();
        setupViews();
    }

    private void setupViews() {
        mAdapter = new AreaAdapter(this, mProvinceList);
        mListView.setAdapter(mAdapter);
        mProvinceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemValue(0, -1);
            }
        });
        mCityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemValue(1, -1);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemValue(mAdapter.getSetion(), position);
            }
        });
    }

    private void selectItemValue(int setion, int position) {

        if (position >= 0) {
            switch (setion) {
                case 0: {
                    City city = mProvinceList.get(position);
                    mProvince = city.getCityName();
                    mProvinceId = city.getId();
                    mProvinceTextView.setText(mProvince);
                    mCityTextView.setText("");
                    mCityList = mCityMap.get(city.getId());
                    mAdapter.setData(1, mCityList);
                    break;
                }
                case 1: {
                    City city = mCityList.get(position);
                    mCity = city.getCityName();
                    mCityId = city.getId();
                    mCityTextView.setText(mCity);

                    Intent intent = getIntent();
                    mLocalUser.updateArea(mProvinceId, mProvince, mCityId, mCity);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
            }
        } else {
            switch (setion) {
                case 0: {
                    mAdapter.setData(0, mProvinceList);
                    mCityTextView.setText("");
                    break;
                }
                case 1: {
                    mAdapter.setData(1, mCityList);
                    break;
                }
            }
        }
    }

    // 获取城市信息
    private void getAddressInfo() {

        //读取城市信息string
        JsonUtils parser = new JsonUtils();
        //打开文件
        String area_str = readAssets(this, "area_android.json");
        mProvinceList = parser.getJSONParserResult(area_str, "area0");
        mCityMap = parser.getJSONParserResultArray(area_str, "area1");
    }

    /**
     * 读取json文件
     *
     * @param context
     * @param fileName
     * @return
     */
    private String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    private void getLocation() {
        new AmapUtils(this, new AmapUtils.GetLoc() {
            @Override
            public void onLoc(final String provinceName, final String cityName, final double latitude, final double longitude) {
                String province = provinceName.length() > 2 ? provinceName.replace("省", "") : provinceName;
                String city = cityName.length() > 2 ? cityName.replaceAll("市", "") : cityName;
                mCurrentAreaTextView.setText(province + city);
            }

            @Override
            public void onLocFailure() {
                mCurrentAreaTextView.setText("定位失败");
            }
        });
    }

    class AreaAdapter extends BaseAdapter {
        private Context mContext;
        private List<City> cityList = new ArrayList<>();
        private int setion;

        public AreaAdapter(Context context, List<City> cityList) {
            this.mContext = context;
            this.cityList = cityList;
        }

        public void setData(int setion, List<City> list) {
            this.setion = setion;
            this.cityList = list;
            notifyDataSetChanged();
        }

        public int getSetion() {
            return setion;
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_area_view, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
                convertView.setClickable(false);
            }
            holder.addressNameTextView.setText(cityList.get(position).getCityName());
            return convertView;
        }
    }

    class ViewHolder {
        @ViewInject(R.id.textview_address_name)
        private TextView addressNameTextView;

        public ViewHolder(View view) {
            ViewUtils.inject(this, view);
        }
    }
}
