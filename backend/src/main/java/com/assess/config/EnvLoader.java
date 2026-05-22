package com.assess.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 从项目根目录加载 .env，注入 System 属性供 Spring ${...} 解析。
 * 已存在于操作系统环境变量中的键不会被覆盖。
 */
public final class EnvLoader {

    private EnvLoader() {
    }

    public static void load() {
        Path dotenvFile = resolveDotenvFile();
        if (dotenvFile == null) {
            throw new IllegalStateException(
                    "未找到 .env 文件。请在项目根目录复制 .env.example 为 .env 并填写配置，"
                            + "或设置 DOTENV_PATH 指向 .env 的绝对路径。"
            );
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(dotenvFile.getParent().toString())
                .filename(dotenvFile.getFileName().toString())
                .ignoreIfMalformed()
                .load();

        for (DotenvEntry entry : dotenv.entries()) {
            String key = entry.getKey();
            if (System.getenv(key) != null) {
                continue;
            }
            if (System.getProperty(key) == null) {
                System.setProperty(key, entry.getValue());
            }
        }
    }

    private static Path resolveDotenvFile() {
        String explicit = System.getenv("DOTENV_PATH");
        if (explicit == null || explicit.isBlank()) {
            explicit = System.getProperty("DOTENV_PATH");
        }
        if (explicit != null && !explicit.isBlank()) {
            Path p = Paths.get(explicit).toAbsolutePath().normalize();
            if (Files.isRegularFile(p)) {
                return p;
            }
            throw new IllegalStateException("DOTENV_PATH 指向的文件不存在: " + p);
        }

        List<Path> candidates = new ArrayList<>();
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        candidates.add(cwd.resolve(".env"));
        candidates.add(cwd.getParent() != null ? cwd.getParent().resolve(".env") : null);

        for (Path candidate : candidates) {
            if (candidate != null && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
    }
}
