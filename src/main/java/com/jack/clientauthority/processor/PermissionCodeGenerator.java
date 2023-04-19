package com.jack.clientauthority.processor;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限代码的生成规则：
 * <p></p>
 *
 * 根据包名、类名、方法名进行生成。包名只保留首字母。
 * <p></p>
 *
 * 此生成规则不能保证生成的code是唯一的，但是考虑到前端需要拿权限code后续决定页面某些元素是否显示，
 * 在尽量保证唯一的情况下，长度尽量的短一些可能会更友好。
 *
 * <p></p>
 *
 * 例如：c.j.a.UserController.helloWorld
 */
public class PermissionCodeGenerator {

    /**
     * 生成权限代码code
     * @param classReference    类的全限定名
     * @param methodName    方法名
     * @return
     */
    public static String generate(String classReference, String methodName) {
        Assert.notNull(classReference, "classReference can not be null");
        Assert.notNull(methodName, "classReference can not be null");

        StringBuilder codeBuilder = new StringBuilder();
        String[] snippetArray = StringUtils.delimitedListToStringArray(classReference, ".");

        String classSimpleName = snippetArray[snippetArray.length - 1];
        List<String> packageSnippetList = new ArrayList<>(Arrays.asList(snippetArray));
        packageSnippetList.remove(packageSnippetList.get(packageSnippetList.size() - 1));
        for (String snippet : packageSnippetList) {
            if (!StringUtils.hasLength(snippet)) {
                continue;
            }

            codeBuilder.append(snippet.toCharArray()[0]).append(".");
        }

        codeBuilder.append(classSimpleName)
                .append(".").append(methodName);

        return codeBuilder.toString();
    }
}
