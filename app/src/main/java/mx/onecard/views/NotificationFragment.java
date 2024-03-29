package mx.onecard.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.onecard.lists.adapters.NotificationsAdapter;
import mx.onecard.lists.items.SimpleItemTouchHelperCallback;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;

public class NotificationFragment extends Fragment {
    public static final String TAG = NotificationFragment.class.getSimpleName();
    private static final NotificationFragment instance = new NotificationFragment();

    private NotificationsAdapter mRecAdapter;
    private User mUser;
    private RecyclerView mRecyclerView;

    public static NotificationFragment getInstance(){
        return instance;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            mUser = User.getActualUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mRecAdapter = new NotificationsAdapter(getActivity(),mUser.getNotificationItems(),null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.frag_notifications_recyclerview);
        mRecyclerView.setAdapter(mRecAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mRecAdapter);
        callback.setDragAndMove(false); // Not able to do drag and drop
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

}
