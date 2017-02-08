package com.rit.sfp.teachboard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by steve on 12/1/2016.
 */

public class ToolsFragment extends Fragment {
    TeachboardView teachBoardView;
    boolean eraseStatus = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tools_fragment_layout, container, false);
        Button closeToolsBtn = (Button) v.findViewById(R.id.closeToolsViewBtn);
        Button downSizeBtn = (Button) v.findViewById(R.id.downSizeBtn);
        final Button colorBtn = (Button) v.findViewById(R.id.colorBtn);
        final Button eraseBtn = (Button) v.findViewById(R.id.eraseBtn);
        Button upSizeBtn = (Button) v.findViewById(R.id.upSizeBtn);

        teachBoardView = (TeachboardView) getActivity().findViewById(R.id.teachBoardView);
        closeToolsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager _fm = getFragmentManager();
                FragmentTransaction _ft = _fm.beginTransaction();
                ToolsFragment toolsFragment = (ToolsFragment) getFragmentManager().findFragmentById(R.id.toolsFragment);
                _ft.remove(toolsFragment).commit();
            }
        });

        upSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teachBoardView.increaseBrush();
            }
        });
        downSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teachBoardView.decreaseBrush();
            }
        });

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eraseStatus == false) {
                    teachBoardView.erase();
                    eraseStatus = true;
                } else {
                    eraseStatus = false;
                    teachBoardView.unErase();
                }
            }
        });
        //Color popup
        LayoutInflater popupInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = popupInflater.inflate(R.layout.color_popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  popupWindow.showAtLocation(popupView,Gravity.CENTER,0,0);

        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.showAtLocation(v, Gravity.LEFT,0,0);
                registerForContextMenu(colorBtn);
                getActivity().openContextMenu(colorBtn);

                teachBoardView.turnWhite();
            }
        });

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.color_context_menu, menu);
    }

    //More code for Tools here!
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.toString()) {
            case "Red":
                teachBoardView.changeColor(Color.RED);
                break;
            case "Orange":
                teachBoardView.changeColor(Color.rgb(255, 85, 0));
                break;
            case "Yellow":
                teachBoardView.changeColor(Color.YELLOW);
                break;
            case "Green":
                teachBoardView.changeColor(Color.GREEN);
                break;
            case "Blue":
                teachBoardView.changeColor(Color.BLUE);
                break;
            case "Purple":
                teachBoardView.changeColor(Color.MAGENTA);
                break;
            case "Grey":
                teachBoardView.changeColor(Color.DKGRAY);
                break;
            case "White":
                teachBoardView.changeColor(Color.WHITE);
                break;
            case "Black":
                teachBoardView.changeColor(Color.BLACK);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

}
