package es.ulpgc.eite.clean.mvp.sample.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import es.ulpgc.eite.clean.mvp.sample.listToDo.ListToDo;
import es.ulpgc.eite.clean.mvp.sample.listDone.ListDone;
import es.ulpgc.eite.clean.mvp.sample.dummy.Dummy;
import es.ulpgc.eite.clean.mvp.sample.dummy.DummyView;


public class App extends Application implements Mediator, Navigator {

  private DummyState toDummyState, dummyToState;
  private ListToDoState toListToDoState, listToDoToState;
  private ListDoneState toListDoneState, listDoneToState;

  @Override
  public void onCreate() {
    super.onCreate();
    toDummyState = new DummyState();
    toDummyState.toolbarVisibility = false;
    toDummyState.textVisibility = false;

    toListToDoState = new ListToDoState();
    toListToDoState.toolbarVisibility = false;
    toListToDoState.textVisibility = false;
    toListToDoState.addBtnVisibility=true;
    toListToDoState.deleteBtnVisibility=false;

    toListDoneState = new ListDoneState();
    toListDoneState.toolbarVisibility = false;
    toListDoneState.textVisibility = false;


  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Mediator //////////////////////////////////////////////////////////////////////

  @Override
  public void startingDummyScreen(Dummy.ToDummy presenter){
    if(toDummyState != null) {
      presenter.setToolbarVisibility(toDummyState.toolbarVisibility);
      presenter.setTextVisibility(toDummyState.textVisibility);
    }
    presenter.onScreenStarted();
  }

  @Override
  public void startingListToDoScreen(ListToDo.ToListToDo presenter){
    if(toListToDoState != null) {
      presenter.setToolbarVisibility(toListToDoState.toolbarVisibility);
      presenter.setTextVisibility(toListToDoState.textVisibility);
      presenter.setAddBtnVisibility(toListToDoState.addBtnVisibility);
      presenter.setDeleteBtnVisibility(toListToDoState.deleteBtnVisibility);

    }
    presenter.onScreenStarted();
  }

  @Override
  public void startingListDoneScreen(ListDone.ToListDone presenter) {
    if(toDummyState != null) {
      presenter.setToolbarVisibility(toDummyState.toolbarVisibility);
      presenter.setTextVisibility(toDummyState.textVisibility);
    }
    presenter.onScreenStarted();
  }
  
  ///////////////////////////////////////////////////////////////////////////////////
  // Navigator /////////////////////////////////////////////////////////////////////


  @Override
  public void goToNextScreen(Dummy.DummyTo presenter) {
    dummyToState = new DummyState();
    dummyToState.toolbarVisibility = presenter.isToolbarVisible();
    dummyToState.textVisibility = presenter.isTextVisible();

    Context view = presenter.getManagedContext();
    if (view != null) {
      view.startActivity(new Intent(view, DummyView.class));
      presenter.destroyView();
    }

  }

  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  private class DummyState {
    boolean toolbarVisibility;
    boolean textVisibility;
  }

  private class ListToDoState {
    boolean toolbarVisibility;
    boolean textVisibility;
    boolean addBtnVisibility;
    boolean deleteBtnVisibility;


  }

  private class ListDoneState {
    boolean toolbarVisibility;
    boolean textVisibility;
  }

}