import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class RecipeTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void recipe_instantiatesCorrectly_true() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    assertEquals(true, testRecipe instanceof Recipe);
  }

  @Test
  public void getName_recipeInstantiatesWithName_String() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    assertEquals("Fried Chicken", testRecipe.getName());
  }

  @Test
  public void equals_returnsTrueIfNameIngredientsAndInstructionsAreSame_true() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    Recipe anotherRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    assertTrue(testRecipe.equals(anotherRecipe));
  }

  @Test
  public void all_returnsAllInstancesOfRecipe_true() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Recipe secondRecipe = new Recipe("Eggs Benedict", "two eggs, english muffin, hollandaise sauce", "poach eggs, toast bread, add sauce", 4);
    secondRecipe.save();
    assertEquals(true, Recipe.all().get(0).equals(testRecipe));
    assertEquals(true, Recipe.all().get(1).equals(secondRecipe));
  }

  @Test
  public void sortByRating_returnsRecipesOrderedByRating() {
    Recipe firstRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 3);
    firstRecipe.save();
    Recipe secondRecipe = new Recipe("Eggs Benedict", "two eggs, english muffin, hollandaise sauce", "poach eggs, toast bread, add sauce", 5);
    secondRecipe.save();
    Recipe thirdRecipe = new Recipe("Cookies", "eggs, cookie mix, butter", "roll dough into balls, bake", 4);
    thirdRecipe.save();
    List<Recipe> newOrder = Recipe.sortByRating();
    assertEquals(newOrder.get(0).getName(), "Eggs Benedict");
  }

  @Test
  public void searchForIngredient_returnsRecipesContainingIngredient() {
    Recipe firstRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, flour", "Soak chicken in batter, deep fry for 10 minutes", 3);
    firstRecipe.save();
    Recipe secondRecipe = new Recipe("Eggs Benedict", "two eggs, english muffin, hollandaise sauce", "poach eggs, toast bread, add sauce", 5);
    secondRecipe.save();
    Recipe thirdRecipe = new Recipe("Cookies", "eggs, cookie mix, butter", "roll dough into balls, bake", 4);
    thirdRecipe.save();
    List<Recipe> containsSearchTerm = Recipe.searchForIngredient("eggs");
    assertEquals(containsSearchTerm.size(), 2);
  }

  @Test
  public void save_assignsIdToObject() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Recipe savedRecipe = Recipe.all().get(0);
    assertEquals(testRecipe.getId(), savedRecipe.getId());
  }

    @Test
  public void find_returnsRecipeWithSameId_secondRecipe() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Recipe secondRecipe = new Recipe("Eggs Benedict", "two eggs, english muffin, hollandaise sauce", "poach eggs, toast bread, add sauce", 4);
    secondRecipe.save();
    assertEquals(Recipe.find(secondRecipe.getId()), secondRecipe);
  }



  @Test
  public void addTag_addsTagToRecipe() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Tag testTag = new Tag("Poultry");
    testTag.save();
    testRecipe.addTag(testTag);
    Tag savedTag = testRecipe.getTags().get(0);
    assertTrue(testTag.equals(savedTag));
  }

  @Test
  public void getTags_returnsAllTags_List() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Tag testTag = new Tag("Poultry");
    testTag.save();
    testRecipe.addTag(testTag);
    List savedTags = testRecipe.getTags();
    assertEquals(savedTags.size(), 1);
  }

  @Test
  public void delete_deletesRecipe_true() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    testRecipe.delete();
    assertEquals(0, Recipe.all().size());
  }

  @Test
  public void delete_deletesAllTagsAndRecipesAssociations() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Tag testTag = new Tag("Poultry");
    testTag.save();
    testRecipe.addTag(testTag);
    testRecipe.delete();
    assertEquals(0, testTag.getRecipes().size());
  }

  @Test
  public void removeTag_removesAssociationWithSpecifiedRecipe() {
    Recipe testRecipe = new Recipe("Fried Chicken", "Raw chicken, batter, eggs, flour", "Soak chicken in batter, deep fry for 10 minutes", 5);
    testRecipe.save();
    Tag testTag = new Tag("Poultry");
    testTag.save();
    testRecipe.removeTag(testTag);
    List savedTags = testRecipe.getTags();
    assertEquals(0, savedTags.size());
  }
}
