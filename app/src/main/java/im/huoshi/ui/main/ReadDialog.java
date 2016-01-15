package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import im.huoshi.R;

/**
 * Created by Lyson on 16/1/1.
 */
public class ReadDialog extends AppCompatDialog {
    public ReadDialog(Context context) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_read_dialog);
    }
}
