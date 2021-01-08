package com.anitachipkar.medicinereminder;

import android.content.Context;
import androidx.annotation.NonNull;
import com.anitachipkar.medicinereminder.data.source.MedicineRepository;
import com.anitachipkar.medicinereminder.data.source.local.MedicinesLocalDataSource;



public class Injection {

    public static MedicineRepository provideMedicineRepository(@NonNull Context context) {
        return MedicineRepository.getInstance(MedicinesLocalDataSource.getInstance(context));
    }
}
