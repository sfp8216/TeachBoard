package com.rit.sfp.teachboard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by steve on 12/1/2016.
 */

public class ToolsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.tools_fragment_layout,container,false);
        Toast.makeText(getActivity().getApplicationContext(),"TOOLS FRAGMENT!!!",Toast.LENGTH_LONG).show();


        Button closeToolsBtn = (Button) v.findViewById(R.id.closeToolsViewBtn);
        Button upSizeBtn = (Button) v.findViewById(R.id.upSizeBtn);
        Button downSizeBtn = (Button) v.findViewById(R.id.downSizeBtn);
        Button colorBtn = (Button) v.findViewById(R.id.colorBtn);
        Button eraseBtn = (Button) v.findViewById(R.id.eraseBtn);


        closeToolsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.i("MEOW","MEPOW");
            FragmentManager _fm = getFragmentManager();
            FragmentTransaction _ft = _fm.beginTransaction();
            ToolsFragment toolsFragment = (ToolsFragment) getFragmentManager().findFragmentById(R.id.toolsFragment);
            _ft.remove(toolsFragment).commit();
            }
        });
        return v;
    }


    //More code for Tools here!

}
