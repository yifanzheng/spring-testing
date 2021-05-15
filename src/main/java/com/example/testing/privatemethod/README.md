### 模拟和测试私有方法

PowerMock 提供提对私有方法的模拟，但是需要把私有方法所在的类放在 @PrepareForTest 注解中。

```java
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService2.class})
@SpringBootTest(classes = TestApplication.class)
public class UserService2Test { 

  // ...
}
```

**示列**

```java
@InjectMocks
private UserService2 userService;

/**
  * 模拟测试方法
  */
@Test
public void testGetName_mockPrivateMethod() throws Exception {
    // mock private method
    String userId = "xxx";
    UserService2 mockUserService = PowerMockito.spy(userService);
    PowerMockito.when(mockUserService, "getNameById", userId)
            .thenReturn("test-" + userId);
    String name = mockUserService.getName(userId);

    Assert.assertEquals("test-" + userId, name);

}

/**
  * 测试私有方法一
  */
@Test
public void testGetNameById() throws InvocationTargetException, IllegalAccessException {
    // test private method
    String userId = "xxx";
    Method getNameById = PowerMockito.method(UserService2.class, "getNameById", String.class);
    Object result = getNameById.invoke(userService, userId);

    Assert.assertEquals("test-" + userId, result);
}

/**
  * 测试私有方法二
  */
@Test
public void testGetNameById_2() throws Exception {
    // test private method
    String userId = "xxx";
    Object result = Whitebox.invokeMethod(userService, "getNameById", userId);
    Assert.assertEquals("test-" + userId, result);
}
```

### 注意

尽管可以使用 PowerMock 测试私有方法，但在使用此技术时我们必须格外谨慎。鉴于我们测试的目的是验证类的行为，因此在单元测试期间，我们应避免更改类的内部行为。模拟技术应应用于类的外部依赖关系，而不是类本身。

如果模拟私有方法对于测试我们的类至关重要，那么通常表明设计不好。