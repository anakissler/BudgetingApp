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

  /**
   * The fragment argument representing the item ID that this fragment represents.
   */
  public static final String ARG_CATEGORY_ID = "category_id";
  public static final String ARG_CATEGORY_NAME = "category_name";

  /**
   * The dummy content this fragment is presenting.
   */
  private Category mItem;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public ItemDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_CATEGORY_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      String categoryName = (getArguments().getString(ARG_CATEGORY_NAME));

      Activity activity = this.getActivity();
      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
          .findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
       appBarLayout.setTitle(categoryName);
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.item_detail, container, false);

    // Show the dummy content as text in a TextView.
    if (mItem != null) {
//      ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
    }

    return rootView;
  }
}
