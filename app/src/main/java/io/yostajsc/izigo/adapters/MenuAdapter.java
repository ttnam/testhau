package io.yostajsc.izigo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.models.MenuItem;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    private Context context;

    public MenuAdapter(Context context, ArrayList<MenuItem> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        MenuItemViewHolder viewHolder;

        try {

            if (convertView == null) {

                convertView = LayoutInflater.from(this.context).inflate(R.layout.item_menu, null);

                viewHolder = new MenuItemViewHolder(convertView);

                convertView.setTag(viewHolder);

            } else viewHolder = (MenuItemViewHolder) convertView.getTag();

            viewHolder.InitData(getItem(position));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class MenuItemViewHolder {

        @BindView(R.id.menu_img)
        ImageView img;
        @BindView(R.id.menu_name)
        TextView txtName;

        public MenuItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void InitData(MenuItem menu) {
            String menuName = menu.getName();

            int icon = menu.getImage();
            if (icon != -1)
                img.setImageResource(icon);
            txtName.setText(menu.getName());

            if (menuName.equalsIgnoreCase("account")) {
                // PrefsUtils utils = new PrefsUtils(activity);
                /*IgUser user = sharedPresUtils.getUserPrefs();
                String name = user.getName();
                if (!name.isEmpty()) {
                    txtName.setText(name);
                }*/
            }
        }
    }
}
