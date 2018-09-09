package hello;

import java.util.Set;

public class Task {
  int id;
  String name;
  Set<String> params;

  public Task() {
  }

  public Task(int id, String name, Set<String> params) {
    this.id = id;
    this.name = name;
    this.params = params;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getParams() {
    return params;
  }

  public void setParams(Set<String> params) {
    this.params = params;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Task task = (Task) o;

    if (id != task.id) {
      return false;
    }
    if (!name.equals(task.name)) {
      return false;
    }
    return params != null ? params.equals(task.params) : task.params == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + name.hashCode();
    result = 31 * result + (params != null ? params.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Task{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", params=" + params +
        '}';
  }
}
