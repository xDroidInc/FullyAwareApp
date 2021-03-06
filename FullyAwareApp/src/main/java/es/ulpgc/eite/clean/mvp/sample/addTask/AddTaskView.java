package es.ulpgc.eite.clean.mvp.sample.addTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.sample.R;
import es.ulpgc.eite.clean.mvp.sample.app.Navigator;
import es.ulpgc.eite.clean.mvp.sample.app.Subject;

public class AddTaskView
        extends GenericActivity<AddTask.PresenterToView, AddTask.ViewToPresenter, AddTaskPresenter>
        implements AddTask.PresenterToView {

    private Toolbar toolbar;
    private Button selectDateBtn;
    private Button selectTimeBtn;
    private Button addTaskBtn;
    private TextView subjectLabel;
    private TextView titleLabel;
    private TextView descriptionLabel;
    private EditText title;
    private EditText description;
    private EditText date;
    private EditText time;
    private Spinner subject;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        //TextView
        subjectLabel = (TextView) findViewById(R.id.subjectLabel);
        titleLabel = (TextView) findViewById(R.id.titleLabel);
        descriptionLabel = (TextView) findViewById(R.id.descriptionLabel);

        //EditText
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Buttons
        selectDateBtn = (Button) findViewById(R.id.selectDateBtn);
        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onSelectDateBtnClicked();
            }
        });

        selectTimeBtn = (Button) findViewById(R.id.selectTimeBtn);
        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onSelectTimeBtnClicked();
            }
        });

        addTaskBtn = (Button) findViewById(R.id.addTaskBtn);

        //Spinner
        subject = (Spinner) findViewById(R.id.subject_spinner);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onAddTaskBtnClicked();

            }
        });

        Calendar c = Calendar.getInstance();
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String year = String.valueOf(c.get(Calendar.YEAR));
        date.setText(day + "/" + month + "/" + year);

    }

    /**
     * Method that initialized MVP objects
     * {@link super#onResume(Class, Object)} should always be called
     */
    @Override
    protected void onResume() {
        super.onResume(AddTaskPresenter.class, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    ///////////////////////////////////////////////////////////////////////////////////
    // Presenter To View /////////////////////////////////////////////////////////////

    @Override
    public void finishScreen() {
        finish();
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void setDateText(String txt) {
        date.setText(txt);
    }

    @Override
    public void setTimeText(String txt) {
        time.setText(txt);
    }

    @Override
    public String getDescription() {
        return description.getText().toString();
    }

    @Override
    public String getDate() {
        return date.getText().toString();

    }

    @Override
    public String getTime() {
        return time.getText().toString();
    }

    @Override
    public String getTaskTitle() {
        return title.getText().toString();
    }

    @Override
    public String getTaskSubject() {
        return subject.getSelectedItem().toString();
    }

    @Override
    public void setSubjectsSpinner() {

        List<Subject> subjects = getPresenter().getSubjects();
        ArrayList<String> subjectNames = new ArrayList<>();
        ArrayList<String> aux = new ArrayList<>();

        // Obtenemos un Iterador y recorremos la lista.
        ListIterator<Subject> iter = subjects.listIterator(subjects.size());

        while (iter.hasPrevious())
            subjectNames.add(iter.previous().getName());

        Collections.reverse(subjectNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjectNames);
        subject.setAdapter(adapter);
    }

    @Override
    public void toolbarChanged(String colour) {
        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.default_color_choice_values));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.default_color_choice_values));
        if (colorPrimaryList.indexOf(colour)!=(-1)){
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
            toolbar.setBackgroundColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
        }
    }

    @Override
    public void initDialog(final String title, final String description, final String deadline, final String subjectName) {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.insert_into_calendar_confirmation_dialog, null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence text = "Task added";
                int duration = Toast.LENGTH_SHORT;

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent intent = getPresenter().writeTaskIntoCalendar(title, description, deadline, subjectName);
                Navigator app = (Navigator) getApplication();
                app.startActivity(intent);

                dialog.dismiss();
                finishScreen();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence text = "Task added";
                int duration = Toast.LENGTH_SHORT;

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                dialog.dismiss();
                finishScreen();

            }
        });

    }

    @Override
    public void setDialogTitle(String title) {
        alertDialog.setTitle(title);
    }

    @Override
    public void showDialog() {
        alertDialog.show();
    }

    @Override
    public void buttonsChanged(String colour) {
        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.default_color_choice_values));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.default_color_choice_values));
        getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
        selectDateBtn.setBackgroundColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
        selectTimeBtn.setBackgroundColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
        addTaskBtn.setBackgroundColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(colour)))));
    }
}
