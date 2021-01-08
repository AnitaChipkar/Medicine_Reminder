package com.anitachipkar.medicinereminder.alarm;


import com.anitachipkar.medicinereminder.BasePresenter;
import com.anitachipkar.medicinereminder.BaseView;
import com.anitachipkar.medicinereminder.data.source.History;
import com.anitachipkar.medicinereminder.data.source.MedicineAlarm;


public interface ReminderContract {

    interface View extends BaseView<Presenter> {

        void showMedicine(MedicineAlarm medicineAlarm);

        void showNoData();

        boolean isActive();

        void onFinish();

    }

    interface Presenter extends BasePresenter {

        void finishActivity();

        void onStart(long id);

        void loadMedicineById(long id);

        void addPillsToHistory(History history);

    }
}
