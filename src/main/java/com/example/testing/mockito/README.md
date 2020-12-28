## 在 Spring 中使用 Mockito 进行单元测试

我们在使用 Spring 开发项目时，都会用到依赖注入。如果程序依赖了外部系统或者不可控组件，比如依赖数据库、网络通信、文件系统等，我们在编写单元测试时，并不需要实际对外部系统进行操作，这时就要将被测试代码与外部系统进行解耦，而这种解耦方法就叫作 “mock”。所谓 “mock” 就是用一个“假”的服务代替真正的服务。

那我们如何来 mock 服务进行单元测试呢？mock 的方式主要有两种：手动 mock 和利用单元测试框架 mock。其中，利用框架 mock 主要是为了简化代码编写。我们这里主要是介绍利用框架 mock，而手动 mock 只是简单介绍。


### 手动 mock

手动 mock 其实就是重新创建一个类继续被 mock 的服务类，并重写里面的方法。在单元测试中，利用依赖注入的方式使用 mock 的服务类替换原来的服务类。具体代码示列如下：

```java
/**
 * UserRepository
 *
 * @author star
 */
@Repository
public class UserRepository {

    /**
     * 模拟从数据库中获取用户信息，实际开发中需要连接真实的数据库
     */
    public User getUser(String name) {
        User user = new User();
        user.setName("testing");
        user.setEmail("testing@outlook.com");

        return user;
    }
}

/**
 * MockUserRepository
 *
 * @author star
 */
public class MockUserRepository extends UserRepository {

    /**
     * 模拟从数据库中获取用户信息
     */
    @Override
    public User getUser(String name) {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }
}

// 进行单元测试
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceManualTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUser_Manual() {
        // 将 MockUserRepository 注入到 UserService 中
        userService.setUserRepository(new MockUserRepository());
        User user = userService.getUser("mock-test-name");
        Assert.assertEquals("mock-test-name", user.getName());
        Assert.assertEquals("mock-test-email", user.getEmail());
    }
}
```

从上面的代码中，我们可以看到手动 mock 需要编写大量的额外代码，同时被测试类也需要提供依赖注入的入口（setter 方法等）。如果被 mock 的类修改了函数名称或者功能，mock 类也要跟着修改，增加了维护成本。

为了提高效率，减少维护成本，我们推荐使用单元测是框架进行 mock。

### 利用框架 mock

这里我们主要介绍 Mokito.mock()、@Mock、@MockBean 这三种方式的 mock。

**Mocito.mock()**

Mocito.mock() 方法允许我们创建类或接口的 mock 对象。然后，我们可以使用 mock 对象指定其方法的返回值，并验证其方法是否被调用。代码示列如下：

```java
@Test
public void testGetUser_MockMethod() {
    // 模拟 UserRepository，测试时不直接操作数据库
    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    // 将 mockUserRepository 注入到 UserService 类中
    userService.setUserRepository(mockUserRepository);

    User mockUser = mockUser();
    Mockito.when(mockUserRepository.getUser(mockUser.getName()))
            .thenReturn(mockUser);

    User user = userService.getUser(mockUser.getName());
    Assert.assertEquals(mockUser.getName(), user.getName());
    Assert.assertEquals(mockUser.getEmail(), user.getEmail());

    // 验证 mockUserRepository.getUser() 方法是否执行
    Mockito.verify(mockUserRepository).getUser(mockUser.getName());
}
```

**@Mock** 

@Mock 是 Mockito.mock() 方法的简写。同样，我们应该只在测试类中使用它。与 Mockito.mock() 方法不同的是，我们需要在测试期间启用 Mockito 注解才能使用 @Mock 注解。

我们可以调用 `MockitoAnnotations.initMocks(this)` 静态方法来启用 Mockito 注解。为了避免测试之间的副作用，建议在每次测试执行之前先进行以下操作：

```java
@Before
public void setup() {
    // 启用 Mockito 注解
    MockitoAnnotations.initMocks(this);
}
```

我们还可以使用另一种方法来启用 Mockito 注解。通过在 @RunWith() 指定 MockitoJUnitRunner 来运行测试：

```java
@RunWith(MockitoJUnitRunner.class)
public class UserServiceMockTest { 

}
```

下面我们来看看如何使用 @Mock 进行服务 mock。代码示列如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    private User mockUser() {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }

    @Before
    public void setup() {
        // 当使用 @Mock 注解时，
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser_MockAnnotation() {
        User mockUser = mockUser();
        Mockito.when(userRepository.getUser(mockUser.getName()))
                .thenReturn(mockUser);

        User user = userService.getUser(mockUser.getName());
        Assert.assertEquals(mockUser.getName(), user.getName());
        Assert.assertEquals(mockUser.getEmail(), user.getEmail());

        // 验证 mockUserRepository.getUser() 方法是否执行
        Mockito.verify(userRepository).getUser(mockUser.getName());
    }

}
```

Mockito 的 @InjectMocks 注解作用是将 @Mock 所修饰的 mock 对象注入到指定类中替换原有的对象。

**@MockBean**

@MockBean 是 Spring Boot 中的注解。我们可以使用 @MockBean 将 mock 对象添加到 Spring 应用程序上下文中。该 mock 对象将替换应用程序上下文中任何现有的相同类型的 bean。如果应用程序上下文中没有相同类型的 bean，它将使用 mock 的对象作为 bean 添加到上下文中。

@MockBean 在需要 mock 特定 bean（例如外部服务）的集成测试中很有用。

要使用 @MockBean 注解，我们必须在 @RunWith() 中指定 SpringRunner 来运行测试。代码示列如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockBeanTest {

    @MockBean
    private UserRepository userRepository;

    private User mockUser() {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }

    @Test
    public void testGetUser_MockBean() {
        User mockUser = mockUser();
        // 模拟 UserRepository
        Mockito.when(userRepository.getUser(mockUser.getName()))
                .thenReturn(mockUser);
        // 验证结果
        User user = userRepository.getUser(mockUser.getName());
        Assert.assertEquals(mockUser.getName(), user.getName());
        Assert.assertEquals(mockUser.getEmail(), user.getEmail());

        Mockito.verify(userRepository).getUser(mockUser.getName());
    }
}
```

最后，这里需要注意的是，Spring test 默认会重用 bean。如果 A 测试使用 mock 对象进行测试，而 B 测试使用原有的相同类型对象进行测试，B 测试在 A 测试之后运行，那么 B 测试拿到的对象是 mock 的对象。一般这种情况是不期望的，所以需要用 @DirtiesContext 修饰上面的测试避免这个问题。






