package im.huoshi.ui.find;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import im.huoshi.R;

/**
 * Created by Lyson on 16/1/13.
 */
public class InterCesDialog extends AppCompatDialog {
    public InterCesDialog(Context context) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_interces_dialog);
    }
}
