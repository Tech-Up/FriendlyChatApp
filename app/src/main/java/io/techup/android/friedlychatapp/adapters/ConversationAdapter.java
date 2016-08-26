package io.techup.android.friedlychatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import io.techup.android.friedlychatapp.R;
import io.techup.android.friedlychatapp.pojo.Message;
import java.util.List;

/**
 * Created by ceosilvajr on 29/06/16.
 */
public class ConversationAdapter extends ArrayAdapter<Message> {

  private List<Message> mMessages;
  private Context mContext;

  public ConversationAdapter(Context context, List<Message> objects) {
    super(context, 0, objects);
    this.mContext = context;
    this.mMessages = objects;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.conversation_item, parent, false);
    Message message = mMessages.get(position);

    ImageView imageView = (ImageView) view.findViewById(R.id.sender_photo);
    TextView textViewSender = (TextView) view.findViewById(R.id.tv_sender_message);
    TextView textViewReceiver = (TextView) view.findViewById(R.id.tv_receiver_message);

    if (message.isMe()) {
      imageView.setVisibility(View.GONE);
      textViewSender.setVisibility(View.GONE);
      textViewReceiver.setVisibility(View.VISIBLE);
      textViewReceiver.setText(message.getMessage());
    } else {
      Glide.with(mContext).load(message.getSenderPhotoUrl()).asBitmap().into(imageView);
      imageView.setVisibility(View.VISIBLE);
      textViewSender.setVisibility(View.VISIBLE);
      textViewReceiver.setVisibility(View.GONE);
      textViewSender.setText(message.getMessage());
    }
    return view;
  }

  @Override public Message getItem(int position) {
    return mMessages.get(position);
  }
}
