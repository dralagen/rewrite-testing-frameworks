package org.openrewrite.java.testing.easymock;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;


import static org.openrewrite.java.Assertions.java;

class EasymockRemoveReplayTest implements RewriteTest {

    @Language("java")
    private static final String INTERFACE_MY_CLASS = """
        interface MyInterface {
            void aMethod();
        }
        """;

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.openrewrite.java.testing.easymock.EasymockRemoveReplay")
            .parser(JavaParser.fromJavaVersion()
                .classpathFromResources(new InMemoryExecutionContext(), "easymock", "easymockclassextension", "junit-jupiter-api")
                .dependsOn(INTERFACE_MY_CLASS));
    }

    @Test
    void shouldRemoveReplayInvocation() {
        rewriteRun(
            //language=java
            java(
                """
                    import org.junit.jupiter.api.Test;
                    import org.easymock.EasyMock;

                    import static org.easymock.EasyMock.replay;

                    public class MyTest {

                        MyInterface aMock;

                        @Test
                        public void test() {
                            aMock = EasyMock.createMock(MyInterface.class);
                            replay(aMock);
                        }
                    }
                    """,

                """
                    import org.junit.jupiter.api.Test;
                    import org.easymock.EasyMock;

                    public class MyTest {

                        MyInterface aMock;

                        @Test
                        public void test() {
                            aMock = EasyMock.createMock(MyInterface.class);
                        }
                    }
                    """

            )
        );
    }

    @Test
    void shouldRemoveReplayInvocation_fromClassExtention() {
        rewriteRun(
            //language=java
            java(
                """
                    import org.junit.jupiter.api.Test;
                    import org.easymock.classextension.EasyMock;

                    import static org.easymock.classextension.EasyMock.replay;

                    public class MyTest {

                        MyInterface aMock;

                        @Test
                        public void test() {
                            aMock = EasyMock.createMock(MyInterface.class);
                            replay(aMock);
                        }
                    }
                    """,

                """
                    import org.junit.jupiter.api.Test;
                    import org.easymock.classextension.EasyMock;

                    public class MyTest {

                        MyInterface aMock;

                        @Test
                        public void test() {
                            aMock = EasyMock.createMock(MyInterface.class);
                        }
                    }
                    """

            )
        );
    }

}
