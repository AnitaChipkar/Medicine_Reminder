package com.anitachipkar.medicinereminder.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.anitachipkar.medicinereminder.Injection;
import com.anitachipkar.medicinereminder.R;
import com.anitachipkar.medicinereminder.report.MonthlyReportActivity;
import com.anitachipkar.medicinereminder.utils.ActivityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicineActivity extends AppCompatActivity {


    @BindView(R.id.compactcalendar_view)
    CompactCalendarView mCompactCalendarView;

    @BindView(R.id.date_picker_text_view)
    TextView datePickerTextView;

    @BindView(R.id.date_picker_button)
    RelativeLayout datePickerButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;

    @BindView(R.id.fab_add_task)
    FloatingActionButton fabAddTask;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.date_picker_arrow)
    ImageView arrow;

    private MedicinePresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", /*Locale.getDefault()*/Locale.ENGLISH);

    private boolean isExpanded = false;
    private DatabaseHelper databaseHelper;
    private List<Medicines> medicinesList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mCompactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);

        mCompactCalendarView.setShouldDrawDaysHeader(true);

        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);

                int day = calendar.get(Calendar.DAY_OF_WEEK);

                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                }
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
                presenter.reload(day);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        setCurrentDate(new Date());
        MedicineFragment medicineFragment = (MedicineFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (medicineFragment == null) {
            medicineFragment = MedicineFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), medicineFragment, R.id.contentFrame);
        }

        //Create MedicinePresenter
        presenter = new MedicinePresenter(Injection.provideMedicineRepository(MedicineActivity.this), medicineFragment);

        //Add popup to add medicine name
        databaseHelper = new DatabaseHelper(this);
        medicinesList.addAll(databaseHelper.getAllMedicines());
        /*if (databaseHelper.getMedicineCount() == 0)
        {*/
            callOpenDialog();
        //}




    }

    public void callOpenDialog() {        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Medicine Name");        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_add_medicines, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.edit_add_med_name);// add a button
        Button buttonAdd = customLayout.findViewById(R.id.button_add);// add a button
        Button buttonDone = customLayout.findViewById(R.id.button_done);// add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (editText.getText().toString().equals(""))
               {
                   editText.setError("Please add atleast one medicine name!");
                   editText.requestFocus();
               }
               else
               {
                   editText.setError(null);

                   sendDialogDataToActivity(editText.getText().toString());
                   editText.setText("");
               }
           }
       });
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }
    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
            // inserting note in db and getting
            // newly inserted note id
            long id = databaseHelper.insertNote(data);

            // get the newly inserted note from db
            Medicines medicines = databaseHelper.getMedicine(id);

            if (medicines != null) {
                // adding new note to array list at 0 position
                medicinesList.add(0, medicines);
                Log.e("List",medicinesList.toString());


                // refreshing the list
              //  mAdapter.notifyDataSetChanged();

               // toggleEmptyNotes();
            }
        Toast.makeText(this, data + " " + "added Successfully", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_stats) {
            Intent intent = new Intent(this, MonthlyReportActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        mCompactCalendarView.setCurrentDate(date);
    }

    public void setSubtitle(String subtitle) {
        datePickerTextView.setText(subtitle);
    }

    @OnClick(R.id.date_picker_button)
    void onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(arrow).rotation(0).start();
        } else {
            ViewCompat.animate(arrow).rotation(180).start();
        }

        isExpanded = !isExpanded;
        appBarLayout.setExpanded(isExpanded, true);
    }
}
