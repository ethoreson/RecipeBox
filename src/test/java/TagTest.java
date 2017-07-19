import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.List;

public class TagTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void tag_instantiatesCorrectly_true() {
    Tag testTag = new Tag("Good for camping");
    assertEquals(true, testTag instanceof Tag);
  }

  @Test
  public void getName_tagInstantiatesWithName_String() {
    Tag testTag = new Tag("Good for camping");
    assertEquals("Good for camping", testTag.getName());
  }

  // @Test
  // public void save_insertsObjectIntoDatabase_Tag() {
  //   Tag testTag = new Tag("Good for camping");
  //   testTag.save();
  //   assertTrue(Tag.all().get(0).equals(testTag));
  // }

  @Test
  public void all_returnsAllInstancesOfTag_true() {
    Tag testTag = new Tag("Good for camping");
    testTag.save();
    Tag secondTag = new Tag("Thanksgiving");
    secondTag.save();
    assertEquals(true, Tag.all().get(0).equals(testTag));
    assertEquals(true, Tag.all().get(1).equals(secondTag));
  }

  @Test
  public void save_assignsIdToObject() {
    Tag testTag = new Tag("Good for camping");
    testTag.save();
    Tag savedTag = Tag.all().get(0);
    assertEquals(testTag.getId(), savedTag.getId());
  }

    @Test
  public void find_returnsTagWithSameId_secondTag() {
    Tag testTag = new Tag("Good for camping");
    testTag.save();
    Tag secondTag = new Tag("Thanksgiving");
    secondTag.save();
    assertEquals(Tag.find(secondTag.getId()), secondTag);
  }

  @Test
  public void getRecipes_returnsAllRecipes_List() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Tag testTag = new Tag("Poultry");
    testTag.save();
    testRecipes.addTag(testTag);
    List savedRecipes = testTag.getRecipes();
    assertEquals(1, savedRecipes.size());
  }

}
