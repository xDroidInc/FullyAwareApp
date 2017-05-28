package es.ulpgc.eite.clean.mvp.sample.listDoneMaster;


import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.sample.app.Mediator;
import es.ulpgc.eite.clean.mvp.sample.app.Navigator;
import es.ulpgc.eite.clean.mvp.sample.app.Task;
import es.ulpgc.eite.clean.mvp.sample.listToDoMaster.ListToDoViewMaster;
import es.ulpgc.eite.clean.mvp.sample.realmDatabase.DatabaseFacade;
import es.ulpgc.eite.clean.mvp.sample.welcome.PrefManager;

public class ListDonePresenterMaster extends GenericPresenter
        <ListDoneMaster.PresenterToView, ListDoneMaster.PresenterToModel, ListDoneMaster.ModelToPresenter, ListDoneModelMaster>
        implements ListDoneMaster.ViewToPresenter, ListDoneMaster.ModelToPresenter, ListDoneMaster.ListDoneTo, ListDoneMaster.ToListDone, ListDoneMaster.MasterListToDetail, ListDoneMaster.DetailToMaster, Observer {


    private boolean toolbarVisible;
    private boolean deleteBtnVisible;
    private boolean textVisible;
    private boolean listClicked;
    private boolean selectedState;
    private Task selectedTask;
    private SparseBooleanArray itemsSelected =new SparseBooleanArray();
    private DatabaseFacade database;




    /**
     * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
     * Responsible to initialize MODEL.
     * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
     * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
     *
     * @param view The current VIEW instance
     */
    @Override
    public void onCreate(ListDoneMaster.PresenterToView view) {
        super.onCreate(ListDoneModelMaster.class, this);
        setView(view);
        Log.d(TAG, "calling onCreate()");


        Log.d(TAG, "calling startingLisToDoScreen()");
        Mediator app = (Mediator) getView().getApplication();
        database =DatabaseFacade.getInstance();
        app.startingListDoneScreen(this);
        app.loadSharePreferences((ListDoneViewMasterTesting) getView());
    }

    /**
     * Operation called by VIEW after its reconstruction.
     * Always call {@link GenericPresenter#setView(ContextView)}
     * to save the new instance of PresenterToView
     *
     * @param view The current VIEW instance
     */
    @Override
    public void onResume(ListDoneMaster.PresenterToView view) {
        setView(view);
        Log.d(TAG, "calling onResume()");

        if (configurationChangeOccurred()) {
            Mediator app = (Mediator) getView().getApplication();
            app.loadSharePreferences((ListDoneViewMasterTesting) getView());
            checkDeleteBtnVisibility();



//            if (buttonClicked) {
//                getView().setText(getModel().getText());
//            }
        }
        loadItems();
    }


    /**
     * Helper method to inform Presenter that a onBackPressed event occurred
     * Called by {@link GenericActivity}
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "calling onBackPressed()");
    }

    /**
     * Hook method called when the VIEW is being destroyed or
     * having its configuration changed.
     * Responsible to maintain MVP synchronized with Activity lifecycle.
     * Called by onDestroy methods of the VIEW layer, like: {@link GenericActivity#onDestroy()}
     *
     * @param isChangingConfiguration true: configuration changing & false: being destroyed
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        super.onDestroy(isChangingConfiguration);
        Log.d(TAG, "calling onDestroy()");
    }


    ///////////////////////////////////////////////////////////////////////////////////
    // View To Presenter /////////////////////////////////////////////////////////////


    @Override
    public void onListClick(View v, int adapterPosition, Task task) {
        if(selectedState){
            if(!v.isSelected()){
                v.setSelected(true);
                itemsSelected.put(adapterPosition,true);

            }else{
                v.setSelected(false);
                itemsSelected.put(adapterPosition,false);

            }
        }else{
            Navigator app = (Navigator) getView().getApplication();
            selectedTask=task;
            app.goToDetailScreen(this);
        }
      checkDeleteBtnVisibility();

    }


    @Override
    public void onLongListClick(View v, int adapterPosition) {
        if(!selectedState){
            selectedState =true;
            v.setSelected(true);
            itemsSelected.put(adapterPosition,true);

        }

        checkDeleteBtnVisibility();




    }
    @Override
    public boolean isSelected(int adapterPosition) {
        boolean result = false;
        if(itemsSelected.size()!=0) {

            if (itemsSelected.get(adapterPosition)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void onBinBtnClick(ListDoneViewMasterTesting.TaskRecyclerViewAdapter adapter) {

        ArrayList<Task> selected = getSelectedTasks(adapter);
        for(int i=0;i<selected.size();i++){
            database.deleteDatabaseItem(selected.get(i));
            //  Log.d(TAG+ "ONBInItem a eliminar", selected.get(i).getTaskId());
        }
        itemsSelected.clear();

        checkDeleteBtnVisibility();


    }

    @Override
    public String getCases(Task task) {
        String subjectName= task.getSubject().getName();

        return  getModel().calculateCases(subjectName);


    }
    private ArrayList<Task> getSelectedTasks(ListDoneViewMasterTesting.TaskRecyclerViewAdapter adapter) {
        ArrayList<Task> selected = new ArrayList<>();
        for(int i=0;i<adapter.getItemCount();i++){
            if(itemsSelected.get(i)){
                selected.add(adapter.getItems().get(i));
            }
        }
        return selected;
    }





    ///////////////////////////////////////////////////////////////////////////////////
    // To ListForgottenDetail //////////////////////////////////////////////////////////////////////

    @Override
    public void onScreenStarted() {
        Log.d(TAG, "calling onScreenStarted()");
   /* if(isViewRunning()) {
      getView().setLabel(getModel().getLabel());
    }
    //checkToolbarVisibility();
    //checkTextVisibility();*/

        checkDeleteBtnVisibility();
        loadItems();
    }



    @Override
    public void setToolbarVisibility(boolean visible) {
        toolbarVisible = visible;
    }

    @Override
    public void setTextVisibility(boolean visible) {
        textVisible = visible;
    }



    @Override
    public void setDeleteBtnVisibility(boolean deleteBtnVisibility) {
        deleteBtnVisible=deleteBtnVisibility;

    }




    ///////////////////////////////////////////////////////////////////////////////////
    // ListForgottenDetail To //////////////////////////////////////////////////////////////////////


    @Override
    public Context getManagedContext() {
        return getActivityContext();
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    @Override
    public boolean getToolbarVisibility() {
        return toolbarVisible;
    }

    @Override
    public void destroyView() {
        if (isViewRunning()) {
            getView().finishScreen();
        }
    }

    @Override
    public boolean isToolbarVisible() {
        return toolbarVisible;
    }

    @Override
    public boolean isTextVisible() {
        return textVisible;
    }


    ///////////////////////////////////////////////////////////////////////////////////

    private void checkToolbarVisibility() {
        Log.d(TAG, "calling checkToolbarVisibility()");
        if (isViewRunning()) {
            if (!toolbarVisible) {
                getView().hideToolbar();
            }
        }
    }
    


    private void checkDeleteBtnVisibility() {
        Log.d(TAG, "calling checkDeleteBtnVisibility()");
        if (isViewRunning()) {
            if (!deleteBtnVisible) {
                getView().hideDeleteBtn();
            } else {
                getView().showDeleteBtn();
            }
        }
    }


    public void setListClicked(boolean listClicked) {
        this.listClicked = listClicked;
    }

    @Override
    public void onLoadItemsTaskStarted() {
        checkToolbarVisibility();

    }

    @Override
    public void onLoadItemsTaskFinished(List<Task> itemsFromDatabase) {
        getView().setRecyclerAdapterContent(itemsFromDatabase);
    }

    /*
        @Override
        public void onLoadItemsSubjectsFinished(List<Task> items) {
            getView().setRecyclerAdapterContent(items);

        }*/
    public void loadItems() {
        /*if(!(database.getValidDatabase()) && !(database.getRunningTask())) {
            startDelayedTask();
        } else {*/
        if(!(database.getRunningTask())){
            Log.d(TAG, "calling onLoadItemsSubjectsFinished() method");
            onLoadItemsTaskFinished(database.getDoneItemsFromDatabase());
        } else {
            Log.d(TAG, "calling onLoadItemsSubjectStarted() method");
            onLoadItemsTaskStarted();
        }
        //}

    }
    /*private void startDelayedTask() {
        Log.d(TAG, "calling startDelayedTask() method");
        database.setRunningTask(true);
        Log.d(TAG, "calling onLoadItemsSubjectStarted() method");
        onLoadItemsSubjectStarted();

        // Mock Hello: A handler to delay the answer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setItems();
                database.setRunningTask(false);
                database.setValidDatabase(true);
                Log.d(TAG, "calling onLoadItemsSubjectsFinished() method");
                //getPresenter().onLoadItemsSubjectsFinished(items);
                onLoadItemsSubjectsFinished(database.getItemsFromDatabase());
            }
        }, 0);
    }*/

    public void reloadItems() {
        //items = null;
        database.deleteAllDatabaseItems();
        database.setValidDatabase(false);
        loadItems();
    }
@Override
public void onErrorDeletingItem(Task item) {

}

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(true)){
            database.deleteDatabaseItem(selectedTask);

            getView().setToastDelete();
        }



    }

    public int getToolbarColour() {
        PrefManager prefManager = new PrefManager(getActivityContext());
        return prefManager.getToolbarColour();
    }
}
