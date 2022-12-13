package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB_Handler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private SQLiteOpenHelper db_handler;
    public PersistentAccountDAO(DB_Handler db_handler) {
        this.db_handler = db_handler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();
        SQLiteDatabase db = db_handler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+DB_Handler.accountNo+" FROM "+DB_Handler.tbl_account,null);
        if(cursor.moveToFirst()){
            do {
                accountNumberList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
//        cursor.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        SQLiteDatabase db = db_handler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_Handler.tbl_account,null);
        if(cursor.moveToFirst()) {
            do{
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3)));
                accountList.add(account);
            }while(cursor.moveToNext());
        }
        db.close();
//        cursor.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_handler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_Handler.tbl_account+" WHERE "+DB_Handler.accountNo+"="+accountNo,null);
        Account account = new Account(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                Double.parseDouble(cursor.getString(3)));
        db.close();
//        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = db_handler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_Handler.accountNo,account.getAccountNo());
        values.put(DB_Handler.bankName,account.getBankName());
        values.put(DB_Handler.accountHolderName,account.getAccountHolderName());
        values.put(DB_Handler.accountBalance,account.getBalance());

        db.insert(DB_Handler.tbl_account,null,values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_handler.getWritableDatabase();
        db.delete(DB_Handler.tbl_account,"accountNo=?",new String[]{accountNo});
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = db_handler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_Handler.tbl_account+" WHERE "+DB_Handler.accountNo+"="+accountNo,null);
        double balance;
        if(expenseType == ExpenseType.EXPENSE){
            balance = Double.parseDouble(cursor.getString(3)) - amount;
        }else {
            balance = Double.parseDouble(cursor.getString(3)) + amount;
        }
        ContentValues values = new ContentValues();
        values.put(DB_Handler.accountBalance,balance);
        db.update(DB_Handler.tbl_account,values,"accountNo=?",new String[]{accountNo});
        db.close();
//        cursor.close();
    }
}
