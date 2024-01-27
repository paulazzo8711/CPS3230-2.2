import enums.XpertStates;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static enums.XpertStates.HomePage;
import static enums.XpertStates.LoggedIn;
import static org.junit.jupiter.api.Assertions.*;

public class XpertTests implements FsmModel {

    XpertOperator sut = new XpertOperator();
    XpertStates state = HomePage;
    boolean isProductSearched = false;
    boolean isProductSelected = false;
    boolean isProductAddedToCart = false;
    boolean isProductRemovedFromCart = false;
    boolean isCartViewed = false;
    boolean isCheckingOut = false;
    boolean isUserLoggedIn = false;
    boolean isUserLoggedOut = true;
    private int cartItemCount = 0;
    private boolean cartCheckedOut = false;

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public void reset(boolean testing) {
        state = HomePage;
        isProductSearched = false;
        isProductSelected = false;
        isProductAddedToCart = false;
        isProductRemovedFromCart = false;
        isCartViewed = false;
        isCheckingOut = false;
        isUserLoggedIn = false;
        isUserLoggedOut = true;
        cartItemCount = 0;
        cartCheckedOut = false;

        if (testing) {
            sut = new XpertOperator();

        }
    }
    public boolean loginUserGuard() {
        return state == HomePage && !sut.isUserLoggedIn();
    }

    public @Action void loginUser() {
        assertTrue(loginUserGuard());
        sut.loginUser("gcvykxqjaivviwofwt@cazlv.com", "password");
        state = XpertStates.LoggedIn;
        isUserLoggedIn = true;
        isUserLoggedOut = false;
        assertTrue(sut.isUserLoggedIn());
        assertFalse(sut.isUserLoggedOut());
        assertEquals(XpertStates.LoggedIn, state);
        state = HomePage;

    }

    public boolean searchGuard() {
        return state == HomePage || state == LoggedIn;

    }

    public @Action void search() {
        assertTrue(searchGuard());
        sut.searchForProduct("Mouse");
        state = XpertStates.SearchResults;
        isProductSearched = true;
        assertTrue(sut.isProductSearched());
        assertEquals(XpertStates.SearchResults, state);

    }

    public boolean selectProductGuard() {
        return state == XpertStates.SearchResults;
    }

    public @Action void selectProduct() {
        assertTrue(selectProductGuard());
        boolean selectResult = sut.selectFirstProduct();
        assertTrue(selectResult);
        state = XpertStates.ProductPage;
    }

    public boolean addToCartGuard() {
        return state == XpertStates.ProductPage;
    }

    public @Action void addToCart() {
        assertTrue(addToCartGuard());
        sut.addProductToCart();
        state = XpertStates.ProductPage;
        cartItemCount++;
    }

    public boolean viewCartGuard() {
        return (state != XpertStates.Cart && cartItemCount > 0) && !isCartViewed;
    }

    public @Action void viewCart() {
        assertTrue(viewCartGuard());
        sut.viewCart();
        state = XpertStates.Cart;
    }

    public boolean checkoutGuard() {
        return state == XpertStates.Cart && cartItemCount > 0;
    }

    public @Action void checkout() {
        assertTrue(checkoutGuard());
        sut.checkout();
        state = XpertStates.Checkout;
        cartCheckedOut = true;
        viewCart();
        state = XpertStates.Cart;
    }

    public boolean removeFromCartGuard() {
        return state == XpertStates.Cart && cartItemCount > 0 && cartCheckedOut;
    }

    public @Action void removeFromCart() {
        assertTrue(removeFromCartGuard());
        sut.removeFromCart();
        cartItemCount = 0;
    }

    public boolean logoutGuard() {
        return sut.isUserLoggedIn() && cartItemCount == 0 && state == XpertStates.Cart;

    }

    public @Action void logout() {
        assertTrue(logoutGuard());
        sut.logoutUser();
        state = HomePage;
    }

    @Test
    public void runModelBasedTests() {

        final GreedyTester greedyTester = new GreedyTester(new XpertTests());
        greedyTester.setRandom(new Random());
        greedyTester.buildGraph();
        greedyTester.addListener(new StopOnFailureListener());
        greedyTester.addListener("verbose");
        greedyTester.addCoverageMetric(new TransitionPairCoverage());
        greedyTester.addCoverageMetric(new StateCoverage());
        greedyTester.addCoverageMetric(new ActionCoverage());

        final RandomTester randomTester = new RandomTester(new XpertTests());
        randomTester.setRandom(new Random());
        randomTester.buildGraph();
        randomTester.addListener(new StopOnFailureListener());
        randomTester.addListener("verbose");
        randomTester.addCoverageMetric(new TransitionPairCoverage());
        randomTester.addCoverageMetric(new StateCoverage());
        randomTester.addCoverageMetric(new ActionCoverage());

        randomTester.generate(100);
        randomTester.printCoverage();
    }

    @AfterEach
    public void tearDown() {
        sut.closeBrowser();
    }

}