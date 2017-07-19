import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_box_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteRecipesQuery = "DELETE FROM recipes *;";
      String deleteTagsQuery = "DELETE FROM tags *;";
      String deleteJoinsQuery = "DELETE FROM recipes_tags *;";
      con.createQuery(deleteRecipesQuery).executeUpdate();
      con.createQuery(deleteTagsQuery).executeUpdate();
      con.createQuery(deleteJoinsQuery).executeUpdate();
    }
  }

}
