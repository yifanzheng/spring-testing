## Spring Testing

此项目包含 Spring 单元测试相关的内容。

- [在 Spring 中使用 Mockito 进行单元测试](src/main/java/com/example/testing/mockito/README.md)

- [在 Spring 中 Mock RestTemplate](src/main/java/com/example/testing/resttemplate/README.md)

- [使用 PowerMock 模拟和测试私有方法](src/main/java/com/example/testing/privatemethod/README.md)

## 测试框架介绍

Mockito 是一个单元测试模拟框架，可以让你写出优雅、简洁的单元测试代码。Mockito 采用了模拟技术，模拟了一些在应用中依赖的复杂对象，从而把测试对象和依赖对象隔离开来。

PowerMock 是一个单元测试模拟框架，是在其它单元测试模拟框架的基础上做出扩展。通过提供定制的类加载器以及一些字节码篡改技术的应用，PowerMock 实现了对**静态方法、构造方法、私有方法以及 final 方法的模拟支持**等强大的功能。但是，正因为 PowerMock 进行了字节码篡改，导致部分单元测试用例并不被 JaCoco 统计覆盖率。

所以，优先推荐使用 Mockito 提供的功能；只有在 Mockito 提供的功能不能满足需求时，才会采用 PowerMock 提供的功能；但是，不推荐使用影响 JaCoco 统计覆盖率的 PowerMock 功能。


## 参考

[1] [Mocking of Private Methods Using PowerMock](https://www.baeldung.com/powermock-private-method)  
[2] [无所不能的PowerMock，mock私有方法，静态方法，测试私有方法，final类](https://www.cnblogs.com/ljw-bim/p/9391770.html)