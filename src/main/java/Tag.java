import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Tag {
  private String name;
  private int id;

  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherTag){
    if (!(otherTag instanceof Tag)) {
      return false;
    } else {
      Tag newTag = (Tag) otherTag;
      return this.getName().equals(newTag.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Tag> all() {
    String sql = "SELECT * FROM tags";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(Tag.class);
    }
  }

  public static Tag find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM tags where id=:id";
    Tag tag = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Tag.class);
    return tag;
  }
}

public List<Recipe> getRecipes() {
  try(Connection con = DB.sql2o.open()){
    String joinQuery = "SELECT recipe_id FROM recipes_tags WHERE tag_id = :tag_id";
    List<Integer> recipeIds = con.createQuery(joinQuery)
      .addParameter("tag_id", this.getId())
      .executeAndFetch(Integer.class);
    List<Recipe> recipes = new ArrayList<Recipe>();
    for (Integer recipeId : recipeIds) {
      String recipeQuery = "SELECT * FROM recipes WHERE id = :recipeId";
      Recipe recipe = con.createQuery(recipeQuery)
        .addParameter("recipeId", recipeId)
        .executeAndFetchFirst(Recipe.class);
      recipes.add(recipe);
    }
    return recipes;
  }
}

}
