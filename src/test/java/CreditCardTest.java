import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import user.CreditCard;

/**
 * Klasa do testów.
 */
public class CreditCardTest
{
    /**
     * Obiekt testowy.
     */
    private static CreditCard card;

    /**
     * Przygotowanie do testu.
     */
    @BeforeClass
    public static void Before()
    {
        card = new CreditCard(1010,1000);
        System.out.println("Junit5 Test");
    }

    /**
     * Sprawdzenie zmiany stanu konta, ktore powinno zakończyć się sukcesem.
     */
    @Test
    @DisplayName("Test changing credit")
    public void changeGoodCredit()
    {
        Assert.assertTrue(card.changeCredit(-100));
    }
    /**
     * Sprawdzenie zmiany stanu konta, ktore powinno zakończyć się niepowodzeniem.
     */
    @Test
    @DisplayName("Test changing credit")
    public void changeBadCredit()
    {
        Assert.assertFalse(card.changeCredit(-1000));
    }
    /**
     * Sprawdzenie weryfikacji PIN, które powinno zakończyć się niepowodzeniem.
     */
    @Test
    @DisplayName("Trying bad PIN verification")
    public void tryBadPIN()
    {
        Assert.assertFalse(card.verifyPIN(1011));
    }
    /**
     * Sprawdzenie nadania nowego PIN, które powinno zakończyć się niepowodzeniem.
     */
    @Test
    @DisplayName("Trying to change PIN to incorrect PIN")
    public void tryBadNewPIN()
    {
        Assert.assertEquals(card.changePIN(1010,-1000),1);
    }
}
