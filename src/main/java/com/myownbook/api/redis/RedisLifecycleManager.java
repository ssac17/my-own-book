package com.myownbook.api.redis;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
@Profile({"local", "dev", "test"})
public class RedisLifecycleManager implements ApplicationListener<ContextClosedEvent> {

    private static final String DOCKER_COMPOSE_YML_RELATIVE_PATH = "docker-compose.yml";

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("[RedisLifecycleManager] Application context closed. Attempting to shut down Redis container...");
        executeDockerComposeDown();
    }

    private void executeDockerComposeDown() {
        System.out.println("[RedisLifecycleManager DEBUG] Starting executeDockerComposeDown process...");
        try {
            // 1. 'docker' 실행 파일 경로 찾기
            String dockerPath = findDockerExecutable();
            if (dockerPath == null) {
                System.err.println("[RedisLifecycleManager ERROR] Could not find 'docker' executable. Cannot shut down Redis.");
                return;
            }
            System.out.println("[RedisLifecycleManager DEBUG] Found 'docker' executable at: " + dockerPath);

            // 2. docker-compose.yml 파일의 절대 경로 얻기
            File dockerComposeYmlFile = new File(System.getProperty("user.dir"), DOCKER_COMPOSE_YML_RELATIVE_PATH);
            String dockerComposeYmlAbsPath = dockerComposeYmlFile.getAbsolutePath();

            if (!dockerComposeYmlFile.exists()) {
                System.err.println("[RedisLifecycleManager ERROR] docker-compose.yml not found at: " + dockerComposeYmlAbsPath + ". Cannot shut down Redis.");
                return;
            }
            System.out.println("[RedisLifecycleManager DEBUG] Using docker-compose.yml at: " + dockerComposeYmlAbsPath);

            ProcessBuilder processBuilder = new ProcessBuilder();
            String[] command = {dockerPath, "compose", "-f", dockerComposeYmlAbsPath, "down"};
            processBuilder.command(command);

            System.out.println("[RedisLifecycleManager DEBUG] Executing command: " + Arrays.toString(command));

            Process process = processBuilder.start();

            // 스트림 고블러를 사용하여 표준 출력 및 에러 출력 읽기
            // JVM이 종료되기 전에 모든 출력 스트림이 비워지도록 합니다.
            StreamGobbler standardOutputGobbler = new StreamGobbler(process.getInputStream(), line -> System.out.println("[docker compose down stdout] " + line));
            StreamGobbler errorOutputGobbler = new StreamGobbler(process.getErrorStream(), line -> System.err.println("[docker compose down stderr] " + line));

            // 데몬 스레드로 실행하여 앱 종료와 무관하게 출력 스트림을 계속 소비
            Executors.newSingleThreadExecutor().submit(standardOutputGobbler);
            Executors.newSingleThreadExecutor().submit(errorOutputGobbler);

            int exitCode = process.waitFor(); // docker compose down 명령이 완료될 때까지 기다림
            System.out.println("[RedisLifecycleManager DEBUG] 'docker compose down' command exited with code: " + exitCode);

            if (exitCode != 0) {
                System.err.println("[RedisLifecycleManager ERROR] Failed to shut down Redis container via 'docker compose down'. Exit code: " + exitCode);
            } else {
                System.out.println("[RedisLifecycleManager] Redis container shut down successfully.");
            }

        } catch (Exception e) {
            System.err.println("[RedisLifecycleManager FATAL ERROR] Exception during 'docker compose down': " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 'docker' 실행 파일 경로를 찾는 헬퍼 메서드
    private String findDockerExecutable() throws Exception {
        String dockerPath = null;
        try {
            Process p = Runtime.getRuntime().exec("which docker");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            dockerPath = input.readLine(); // 첫 번째 줄만 읽음
            input.close();
            p.waitFor(); // 'which docker' 명령이 완료될 때까지 기다림
        } catch (Exception e) {
            System.err.println("Error finding 'docker' with 'which': " + e.getMessage());
        }

        if (dockerPath != null && !dockerPath.trim().isEmpty()) {
            return dockerPath.trim();
        }

        // Common macOS/Linux path fallback if 'which' fails (e.g., in a non-standard shell env)
        File commonDockerPath = new File("/usr/local/bin/docker");
        if (commonDockerPath.exists() && commonDockerPath.canExecute()) {
            System.err.println("[RedisLifecycleManager DEBUG] Fallback: Found executable at " + commonDockerPath.getAbsolutePath());
            return commonDockerPath.getAbsolutePath();
        }

        System.err.println("[RedisLifecycleManager ERROR] Could not find docker executable via 'which' or common paths.");
        return null;
    }

    // 프로세스 출력 스트림을 소비하기 위한 유틸리티 클래스 (데드락 방지)
    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream; // Changed to InputStream
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                reader.lines().forEach(consumer);
            } catch (Exception e) {
                System.err.println("Error reading stream by gobbler: " + e.getMessage());
            }
        }
    }
}