## 在 Spring 中 Mock RestTemplate

如果我们程序中使用了 RestTemplate 进行 HTTP API 调用。通常在编写单元测试时，为了让测试可控，会将 RestTemlate 调用进行 mock，而不是进行真实的 HTTP API 调用。

这里，我们将介绍两种 mock RestTemplate 调用的方法。一个是比较流行的 Mockito 模拟库，另一个是使用 Spring Test 提供的 MockRestServiceServer 模拟服务器，它可以创建模拟服务器以定义服务器交互。

### 使用 Mockito 模拟

使用 Mockito 模拟 RestTemplate 测试我们的服务将像其他任何涉及模拟的测试一样简单，下面我们就来看看怎么使用吧。

下面这段代码描述的是，一个 UserService 类，该类通过 HTTP 请求获取用户信息。

```java
@Service("userServiceRest")
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public List<UserVO> getUsers() {
        UserVO[] users = restTemplate.getForObject("http://localhost:8080/users", UserVO[].class);
        if (users == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(users);
    }
}
```
现在，我们要为 UserService 类的 getUsers() 编写单元测试。测试代码如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    
    // 模拟一个假的 RestTemplate 实例
    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("userServiceRest")
    @InjectMocks
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUsers() {
        UserVO mockUser = new UserVO(1, "mock-test");
        // 模拟一个假的请求
        Mockito.when(restTemplate.getForObject("http://localhost:8080/users", UserVO[].class))
                .thenReturn(new UserVO[]{mockUser});
        List<UserVO> users = userService.getUsers();
        Assert.assertFalse(CollectionUtils.isEmpty(users));
        Assert.assertEquals(1, users.size());
        UserVO userVO = users.get(0);
        Assert.assertEquals(mockUser.getId(), userVO.getId());
        Assert.assertEquals(mockUser.getName(), userVO.getName());
    }
}
```
上面的单元测试中，我们首先使用 Mockito 模拟库中 @Mock 注解创建一个假 RestTemplate 实例。

然后，我们使用 @InjectMocks 注释了 UserService 实例，以将模拟的实例注入到其中。

最后，在测试方法中，我们使用 Mockito 的 when() 和 then() 方法定义了模拟的行为。


### 使用 Spring Test 模拟

Spring Test 模块中包含一个叫 MockRestServiceServer 的模拟服务器。通过这种方法，我们将服务器配置为在通过 RestTemplate 实例调度特定请求时返回特定对象。最后，我们可以在该服务器实例上调用 verify() 方法验证是否满足所有期望。

MockRestServiceServer 实际上是通过使用 MockClientHttpRequestFactory 拦截 HTTP API 调用来工作的。根据我们的配置，它会创建预期请求和相应的响应列表。当 RestTemplate 实例调用 API 时，它将在期望列表中查找请求并返回相应的响应。因此，它无需在任何其他端口上运行 HTTP 服务器来发送模拟响应。

下面，我们使用 MockRestServiceServer 为 UserService 类的 getUsers() 编写单元测试，代码如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockRestServiceServerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetUsers() throws Exception {
        UserVO mockUser = new UserVO(1, "mock-test");
        // 模拟 RestTemplate 请求
        mockServer.expect(ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(new URI("http://localhost:8080/users")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new UserVO[]{mockUser}))
                );
        List<UserVO> users = userService.getUsers();
        Assert.assertFalse(CollectionUtils.isEmpty(users));
        UserVO userVO = users.get(0);
        Assert.assertEquals(mockUser.getId(), userVO.getId());
        Assert.assertEquals(mockUser.getName(), userVO.getName());
        mockServer.verify();
    }

}
```
上面代码段中，我们使用 MockRestRequestMatchers 和 MockRestResponseCreators 中的静态方法以清晰易读的方式定义 REST 调用的期望和响应：

```java
MockRestRequestMatchers.requestTo(new URI("http://localhost:8080/users"));

MockRestRequestMatchers.method(HttpMethod.GET);

MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new UserVO[]{mockUser});
```

这里需要提醒大家的是，测试类中的 RestTemplate 应该与 UserService 类中使用的实例相同。为了确保这一点，我们需要在 spring 容器中定义 RestTemplate bean，并在测试和实现中自动连接实例。

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

当我们编写集成测试并且只需要模拟外部 HTTP 调用时，使用 MockRestServiceServer 非常有用。





