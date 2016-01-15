package im.huoshi.ui.find;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import im.huoshi.R;

/**
 * Created by Lyson on 16/1/13.
 */
public class SuffrageDialog extends AppCompatDialog {
    public SuffrageDialog(Context context) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_suffrage_dialog);
    }
}
