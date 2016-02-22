package im.huoshi.ui.me;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import im.huoshi.R;

/**
 * Created by Lyson on 16/2/3.
 */
public class NickNameDialog extends AppCompatDialog {
    private INickName mINickName;

    public void setINickName(INickName mNickName) {
        this.mINickName = mNickName;
    }

    public NickNameDialog(final Context context, String oldNickName) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_nickname_dialog);
        final EditText nickNameEdit = (EditText) findViewById(R.id.edit_setting_nickname);
        Button cancleButton = (Button) findViewById(R.id.button_cancle);
        Button confirmButton = (Button) findViewById(R.id.button_confirm);
        nickNameEdit.setText(oldNickName);
        nickNameEdit.setSelection(oldNickName.length());
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNickName = nickNameEdit.getText().toString();
                if (TextUtils.isEmpty(newNickName)) {
                    Toast.makeText(context, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mINickName != null) {
                    mINickName.OnNickName(newNickName);
                    dismiss();
                }
            }
        });
    }

    public interface INickName {
        void OnNickName(String newNickName);
    }
}
