package com.cn.db.dbdoc.generator;

import com.cn.db.dbdoc.enums.GenerateType;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author linht
 */
public class GeneratorFactory {
    private static final Map<GenerateType, GeneratorService> OPERATOR_SERVICE_MAP = new HashMap<>(5);

    public static void register(GeneratorService generatorService) {
        GenerateType type = generatorService.type();
        Assert.isTrue(!OPERATOR_SERVICE_MAP.containsKey(type), "GeneratorService出现重复的类型，其类型为：" + type);
        if (Objects.nonNull( type)) {
            OPERATOR_SERVICE_MAP.put(type, generatorService);
        }
    }

    /**
     * 找相关的处理方式
     *
     * @param type
     * @return
     */
    public static GeneratorService get(String type) {
        GenerateType key = GenerateType.valueOf(type);
        Assert.isTrue(OPERATOR_SERVICE_MAP.containsKey(key), "没有找到该类型处理方式" + type);
        return OPERATOR_SERVICE_MAP.get(key);
    }

}
