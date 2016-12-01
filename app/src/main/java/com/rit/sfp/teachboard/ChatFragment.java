package com.rit.sfp.teachboard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by steve on 12/1/2016.
 */

public class ChatFragment extends Fragment {
    static final  String[] CHATTEMP = new String[] {"Admin: hi","Bob: Good day","Admin: how are you?","Bob: Im good thanks","Admin: well this was a great coversation","Bob: Yup, have a good day bye!"};
    ListView chatListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.chat_fragment_layout,container,false);
        Toast.makeText(getActivity().getApplicationContext(),"CHAT FRAGMENT!!!!",Toast.LENGTH_LONG).show();

        //Close the chatWindow
        Button closeChatBtn = (Button) v.findViewById(R.id.closeChatViewBtn);
        closeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MEOW", "BYE CHAT");
                FragmentManager _fm = getFragmentManager();
                FragmentTransaction _ft = _fm.beginTransaction();
                ChatFragment chatFragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chatFragment);
                _ft.remove(chatFragment).commit();
            }
        });

        //Populatee chat
         chatListView = (ListView) v.findViewById(R.id.chatListView);
        ArrayAdapter<String> chat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,CHATTEMP);
        chatListView.setAdapter(chat);


        //Set OffFocus for editText
        EditText editText = (EditText) v.findViewById(R.id.chatText);
     //   View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager m = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });



        return v;
    }


    //More code for Tools here!


}
