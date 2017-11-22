package edu.cnm.deepdive.ak.budgetingapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.ak.budgetingapp.entities.Category;

/**
 * A fragment representing a single Item detail screen. This fragment is either contained in a
 * {@link CategoryListActivity} in two-pane mode (on tablets) or a {@link ItemDetailActivity} on
 * handsets.
 */
public class ItemDetailFragment extends Fragment {

  /**The fragment argument representing the item ID that this fragment represents.*/
  public static final String ARG_CATEGORY_ID = "category_id";
  /**The fragment argument representing the item name that this fragment represents.*/
  public static final String ARG_CATEGORY_NAME = "category_name";

  /**
   * The content this fragment is presenting.
   */
  private Category mItem;

  /**
   * fragment manager to instantiate the fragment
   */
  public ItemDetailFragment() {
  }

  /**
   * Saves the data so that it can be passed back to the activity
   * @param savedInstanceState
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_CATEGORY_ID)) {
      String categoryName = (getArguments().getString(ARG_CATEGORY_NAME));

      Activity activity = this.getActivity();
      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
          .findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
       appBarLayout.setTitle(categoryName);
      }
    }
  }

  /**
   * Creates a view
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return view
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);

    if (mItem != null) {
    }

    return rootView;
  }
}
