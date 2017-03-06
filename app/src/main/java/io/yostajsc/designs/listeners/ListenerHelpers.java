package io.yostajsc.designs.listeners;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yosta.materialdialog.ChoiceDialog;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.MenuAdapter;
import io.yostajsc.izigo.models.menu.MenuItem;
import io.yostajsc.utils.AppUtils;

/**
 * Created by Phuc-Hau Nguyen on 9/6/2016.
 */
public class ListenerHelpers {

    public static CompoundButton.OnCheckedChangeListener SwitchSync = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                MenuAdapter internetAdapter = AppUtils.LoadListMenuAction(buttonView.getContext(),
                        R.array.internet_item_text,
                        R.array.internet_item_icon);

                new ChoiceDialog(buttonView.getContext())
                        .setTopColorRes(R.color.FireBrick)
                        .setTitle(R.string.str_action_change_language_title)
                        .setIcon(R.drawable.ic_vector_sync)
                        .setMessage(buttonView.getContext().getResources().getString(R.string.str_action_change_language_message))
                        .setItems(internetAdapter, new ChoiceDialog.OnItemSelectedListener<MenuItem>() {
                            @Override
                            public void onItemSelected(int position, MenuItem item) {
                                //StorageUtils.KEY_LANGUAGE_MODE = position;
                            }
                        })
                        .show();
            }
        }
    };
    public static View.OnClickListener onRating = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public static TextView.OnEditorActionListener onEditorSearchChange = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(v.getContext(), "No", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    };

    private static String mFileName = "";
    private static Context mContext;
    private static String mType;
    /*private static Target downLoadImageFile = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ContextWrapper cw = new ContextWrapper(mContext);
                        File directory = cw.getDir("user", Context.MODE_PRIVATE);
                        File file = new File(directory, mFileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                        StorageUtils.saveSetting(mContext, mType, directory.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public static void saveImage(Context context, @NonNull String type, @NonNull String url, @NonNull String fileName) {
        mFileName = fileName;
        mContext = context;
        mType = type;
        Picasso.with(context).load(url).into(downLoadImageFile);
    }*/

}
