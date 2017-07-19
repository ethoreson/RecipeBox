import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
  private String name;
  private String ingredients;
  private String instructions;
  private int rating;
  private int id;

  public Recipe(String name, String ingredients, String instructions, int rating) {
    this.name = name;
    this.ingredients = ingredients;
    this.instructions = instructions;
    this.rating = rating;
  }

  public String getName() {
    return name;
  }

  public String getIngredients() {
    return ingredients;
  }

  public String getInstructions() {
    return instructions;
  }

  public int getRating() {
    return rating;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherRecipe){
    if (!(otherRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) otherRecipe;
      return this.getName().equals(newRecipe.getName()) &&
             this.getIngredients().equals(newRecipe.getIngredients()) &&
             this.getInstructions().equals(newRecipe.getInstructions());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (name, ingredients, instructions, rating) VALUES (:name, :ingredients, :instructions, :rating)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("ingredients", this.ingredients)
        .addParameter("instructions", this.instructions)
        .addParameter("rating", this.rating)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Recipe> all() {
    String sql = "SELECT * FROM recipes";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  public static Recipe find(int id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM recipes where id=:id";
    Recipe recipe = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Recipe.class);
    return recipe;
  }
}

  public static List<Recipe> sortByRating(){
      String sql = "SELECT * FROM recipes ORDER BY rating DESC;";
      try(Connection con = DB.sql2o.open()){
        return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  public static List<Recipe> searchForIngredient(String searchTerm){
    String sql = "SELECT * FROM recipes WHERE ingredients LIKE '%" + searchTerm + "%';";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql)
        .executeAndFetch(Recipe.class);
    }
  }

  public void addTag(Tag tag) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes_tags (recipe_id, tag_id) VALUES (:recipe_id, :tag_id)";
      con.createQuery(sql)
      .addParameter("recipe_id", this.getId())
      .addParameter("tag_id", tag.getId())
      .executeUpdate();
    }
  }

  public List<Tag> getTags() {
  try(Connection con = DB.sql2o.open()){
    String joinQuery = "SELECT tag_id FROM recipes_tags WHERE recipe_id = :recipe_id";
    List<Integer> tagIds = con.createQuery(joinQuery)
      .addParameter("recipe_id", this.getId())
      .executeAndFetch(Integer.class);
    List<Tag> tags = new ArrayList<Tag>();
    for (Integer tagId : tagIds) {
      String tagQuery = "SELECT * FROM tags WHERE id = :tagId";
      Tag tag = con.createQuery(tagQuery)
        .addParameter("tagId", tagId)
        .executeAndFetchFirst(Tag.class);
      tags.add(tag);
    }
    return tags;
  }
}


public void delete() {
  try(Connection con = DB.sql2o.open()) {
  String sql = "DELETE FROM recipes WHERE id = :id;";
  con.createQuery(sql)
    .addParameter("id", this.id)
    .executeUpdate();
  String joinDeleteQuery = "DELETE FROM recipes_tags WHERE recipe_id = :recipeId";
  con.createQuery(joinDeleteQuery)
    .addParameter("recipeId", this.getId())
    .executeUpdate();
  }
}

  public void removeTag(Tag tag){
    try(Connection con = DB.sql2o.open()){
      String joinRemovalQuery = "DELETE FROM recipes_tags WHERE recipe_id = :recipeId AND tag_id = :tagId;";
      con.createQuery(joinRemovalQuery)
        .addParameter("recipeId", this.getId())
        .addParameter("tagId", tag.getId())
        .executeUpdate();
    }
  }


}
