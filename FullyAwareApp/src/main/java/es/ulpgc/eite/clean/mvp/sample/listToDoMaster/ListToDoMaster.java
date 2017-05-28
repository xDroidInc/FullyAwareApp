package es.ulpgc.eite.clean.mvp.sample.listToDoMaster;

import android.content.Context;
import android.view.View;
import java.util.List;
import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Task;


public interface ListToDoMaster {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface ToListToDo {
    void onScreenStarted();
    void setToolbarVisibility(boolean visible);
    void setTextVisibility(boolean visible);
    void setAddBtnVisibility(boolean addBtnVisibility);
    void setDeleteBtnVisibility(boolean deleteBtnVisibility);
    void setDoneBtnVisibility(boolean deleteBtnVisibility);
  }

  interface ListToDoTo {
    Context getManagedContext();
    void destroyView();
    boolean isToolbarVisible();
    boolean isTextVisible();
  }
  /**
   * Interfaz que permite iniciar la pantalla del detalle y recopilar los valores necesarios
   * para rellenar el estado inicial que se pasará a la pantalla del detalle al iniciarse
   */
   interface MasterListToDetail{
    Context getManagedContext();
    Task getSelectedTask();
    boolean getToolbarVisibility();

  }
  /**
   * Interfaz que permite fijar los valores incluidos en el estado pasado desde la pantalla
   * del detalle cuando está finaliza
   */
  interface DetailToMaster {
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {

    void onDoneBtnClick(ListToDoViewMaster.TaskRecyclerViewAdapter adapter);


      void onListClick2(View item, int position, ListToDoViewMaster.TaskRecyclerViewAdapter adapter, Task task);

    void onLongListClick2(View item, int adapterPosition);

      void onAddBtnClick();

      boolean isSelected(int adapterPosition);

    void onBinBtnClick2(ListToDoViewMaster.TaskRecyclerViewAdapter adapter);

      String getCases(Task task);
    void setTextWhenIsEmptyVisibility(boolean textWhenIsEmptyVisibility);

    void subjectFilter();

    void swipeLeft(Task currentTask);

    void swipeRight(Task currentTask);

    boolean isTaskForgotten(String deadline);

    void onBtnBackPressed();
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
    void hideToolbar();


      void showAddBtn();

      void hideDeleteBtn();

      void showDeleteBtn();


    boolean isItemListChecked(int pos);

    void setItemChecked(int pos, boolean checked);

    void startSelection();


      void hideAddBtn();

    void hideDoneBtn();

    void showDoneBtn();

      void hideTextWhenIsEmpty();

    void showTextWhenIsEmpty();

    void deselect(int i, boolean b);

  void setRecyclerAdapterContent(List<Task> items);


    void toolbarChanged(String colour);

    void setToastDelete();

    void confirmBackPressed();

    void initSwipe();

    void initDialog();

    void showToastBackConfirmation(String toastBackConfirmation);
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    String getToastBackConfirmation();

    void deleteItem(Task item);
    void loadItems();

    void startBackPressed();

    void reloadItems();
    void setDatabaseValidity(boolean valid);
    String getErrorMessage();
    void addInitialTasks();

    void deleteTestItems();

    void deleteDatabaseItem(Task item);

    String calculateCases(String subjectName);

    List<Task> orderSubjects();
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    Context getManagedContext();
    void onErrorDeletingItem(Task item);
    void onLoadItemsTaskFinished(List<Task> items);
    void onLoadItemsTaskStarted();

    void confirmBackPressed();

    void delayedTaskToBackStarted();
  }


}
