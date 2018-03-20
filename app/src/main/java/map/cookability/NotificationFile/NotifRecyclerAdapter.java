package map.cookability.NotificationFile;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import map.cookability.R;

/**
 * Created by yanglili on 3/17/18.
 */

public class NotifRecyclerAdapter extends RecyclerView.Adapter<NotifRecyclerAdapter.ViewHolder>{
    private List<Notifications> notificationList;

    private Context context;

    public NotifRecyclerAdapter(Context context, List<Notifications> notificationList){
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_list_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String fromId = notificationList.get(position).getFromId();
        final String message = notificationList.get(position).getMessage();
        final String fromName = notificationList.get(position).getFromName();
        final String fromImage = notificationList.get(position).getFromImage();
        final String read = notificationList.get(position).getRead();
        final String notificationId = notificationList.get(position).getNotificationId();
        final String currentId = notificationList.get(position).getCurrentId();
        final String currentName = notificationList.get(position).getCurrentName();
        final String currentImage = notificationList.get(position).getCurrentImage();
        final String abstr = notificationList.get(position).getAbstr();


        final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        GradientDrawable shape1 = new GradientDrawable();
        shape1.setCornerRadius(30);
        shape1.setColor(context.getResources().getColor(R.color.btnBright));

        GradientDrawable shape2 = new GradientDrawable();
        shape2.setCornerRadius(30);
        shape2.setColor(context.getResources().getColor(R.color.btnLight));

        holder.notif_message_view.setText("An appointment from" + fromName + "!");


        if (abstr.equals("appointment")){
            holder.notif_message_view.setText("An appointment from " + fromName + "!");
        }else if (abstr.equals("accept")){
            holder.notif_message_view.setText(fromName + " accepts your appointment" + "!");
        }else if (abstr.equals("decline")){
            holder.notif_message_view.setText(fromName + " declines your appointment" + "!");
        }

        if (read.equals("false")){
            holder.notif_response.setBackground(shape1);
            holder.notif_message_view.setTextColor(context.getResources().getColor(R.color.notifBright));
        }else if (read.equals("true")){
            holder.notif_response.setBackground(shape2);
            holder.notif_message_view.setTextColor(context.getResources().getColor(R.color.notifBright));
        }

        CircleImageView notif_image_view = holder.notif_image_view;
        Glide.with(context).load(notificationList.get(position).getFromImage()).into(notif_image_view);

        holder.notif_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("read", "true");
                mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Notification").document(notificationId).update(map);

                Intent intent = new Intent(context, ShowNotifActivity.class);
                intent.putExtra("fromId", fromId);
                intent.putExtra("fromName",fromName);
                intent.putExtra("message", message);
                intent.putExtra("currentId",currentId);

                intent.putExtra("message", message);
                intent.putExtra("currentId",currentId);
                intent.putExtra("currentName",currentName);
                intent.putExtra("currentImage", currentImage);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private CircleImageView notif_image_view;
        private TextView notif_message_view;
        private Button notif_response;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            notif_image_view = (CircleImageView) mView.findViewById(R.id.notif_list_image);
            notif_message_view = (TextView) mView.findViewById(R.id.notif_list_message);
            notif_response = (Button) mView.findViewById(R.id.show_notif_btn);
        }

    }
}
