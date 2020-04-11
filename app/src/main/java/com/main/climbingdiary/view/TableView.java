package com.main.climbingdiary.view;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Colors;
import com.main.climbingdiary.models.Styles;

import java.util.Set;
import java.util.TreeSet;

public class TableView {
    private Context context;
    private TableLayout stk;
    private View view;
    private ScrollView tableScrollView;

    public TableView(Context _context, View _view){
        this.context = _context;
        this.view = _view;
        this.stk = (TableLayout) this.view.findViewById(R.id.route_table);
        this.tableScrollView = (ScrollView) view.findViewById(R.id.TableScrollView);
    }
    public void show(){
        this.tableScrollView.setVisibility(View.VISIBLE);
    }
    public void hide(){
        this.tableScrollView.setVisibility(View.GONE);
    }
    public void createTableView(){
        //Variables
        String[] styles = Styles.getStyle(true);
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor cursor = taskRepository.getTableValues();
        //tree et because this sort the values
        Set<String> mLevels = new TreeSet<>();
        TableLayout stk = (TableLayout) view.findViewById(R.id.route_table);
        //clear view
        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        tv0.setText("Grad");
        tv0.setTextAppearance(view.getContext(), R.style.TableHeader);
        tbrow0.addView(tv0);
        for(int x=0; x<=styles.length-1;x++){
            TextView tv_style = new TextView(context);
            tv_style.setText(styles[x]);
            tv_style.setPadding(20,10,20,10);
            tv_style.setTextAppearance(view.getContext(), R.style.TableHeader);
            tbrow0.addView(tv_style);
        }
        TextView tv3 = new TextView(context);
        tv3.setText("GESAMT");
        tv3.setTextAppearance(view.getContext(), R.style.TableHeader);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        //variables
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                //get the values
                String level = cursor.getString(0);
                String os = cursor.getString(1);
                String rp = cursor.getString(2);
                String flash = cursor.getString(3);
                String gesamt = cursor.getString(4);
                //append the text view rows
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText(level);
                t1v.setBackgroundColor(Colors.getGradeColor(level));
                t1v.setTextAppearance(view.getContext(), R.style.TableRow);
                t1v.setPadding(30,10,20,10);
                tbrow.addView(t1v);
                TextView t2v = new TextView(context);
                t2v.setText(os);
                t2v.setBackgroundColor(Colors.getGradeColor(level));
                t2v.setTextAppearance(view.getContext(), R.style.TableRow);
                t2v.setPadding(30,10,20,10);
                tbrow.addView(t2v);
                TextView t3v = new TextView(context);
                t3v.setText(rp);
                t3v.setBackgroundColor(Colors.getGradeColor(level));
                t3v.setTextAppearance(view.getContext(), R.style.TableRow);
                t3v.setPadding(30,10,20,10);
                tbrow.addView(t3v);
                TextView t4v = new TextView(context);
                t4v.setText(flash);
                t4v.setBackgroundColor(Colors.getGradeColor(level));
                t4v.setTextAppearance(view.getContext(), R.style.TableRow);
                t4v.setPadding(30,10,20,10);
                tbrow.addView(t4v);
                TextView t5v = new TextView(context);
                t5v.setText(gesamt);
                t5v.setBackgroundColor(Colors.getGradeColor(level));
                t5v.setTextAppearance(view.getContext(), R.style.TableRow);
                t5v.setPadding(30,10,20,10);
                tbrow.addView(t5v);
                stk.addView(tbrow);
                cursor.moveToNext();
            }
        }
    }

}
