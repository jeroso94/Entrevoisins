
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService service;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        service = DI.getNeighbourApiService();
        assertThat(mActivity, notNullValue());
    }

    /**
     * -- NEIGHBOURS TESTS
     */

    /**
     * GIVEN - We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * GIVEN - When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the total number of elements is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * TESTED - NEIGHBOUR ACTIVITY INTENDS with success
     * We ensure that neighbour profile activity intents when an item clicked
     */
    @Test
    public void launch_neighbourgActivity_onItemClick_withSuccess() {
        // From the list of neighbours, the second item is selected
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Then : The correct layout is checked through it's ressource ID
        onView(withId(R.id.neighbour_profile_container)).check(matches(isDisplayed()));
    }

    /**
     * TESTED - NEIGHBOUR ACTIVITY - THE RIGHT NEIGHBOUR'S NAME DISPLAYS
     * We ensure that the neighbour name displays on TextView is correct
     */
    @Test
    public void neighbourName_isCorrect() {
        // From the list of neighbours, the second item is selected
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Then : The dataset item name is compared with the ViewInteraction object (a TextView filled with item name)
        ViewInteraction textView = onView(withId(R.id.name));
        textView.check(matches(withText(service.getNeighbours().get(1).getName())));
    }


    /**
     * -- FAVORITE TESTS
     */

    /**
     * We ensure that Favorite tab only displays Favorites items
     */
    @Test
    public void display_FavoriteItemsOnFavoriteTab_withSuccess() {
        // Step 1 : From the list of neighbours, the second item is selected
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Step 2 : The FAB Button is clicked to add the item as favorite
        onView(withId(R.id.floatingActionButton))
                .perform(click());
        // Step 3 : The Action Bar Up button is clicked
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());
        // Step 4 : The favorite Tab is selected
        onView(withContentDescription("Favorites"))
                .perform(click());
        //Then : (given that a favorite already exist) The ItemCount must be equal to '2'
        onView(allOf(ViewMatchers.withId(R.id.list_favorite), isDisplayed())).check(withItemCount(2));
    }

}