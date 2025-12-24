# SPRING PLUS

---
# 서비스 아키텍처
<img width="1007" height="554" alt="image" src="https://github.com/user-attachments/assets/0bc3ae42-5d59-4dc2-9d81-1ac7721ff862" />

---
# AWS 활용

## EC2
### EC2 인스턴스 요약
<img width="1610" height="598" alt="image" src="https://github.com/user-attachments/assets/1d130ad9-cb05-4c0c-8247-997c8b52f236" />

### EC2 생성
1. 이름 및 Ubuntu 설정
<img width="323" height="556" alt="image" src="https://github.com/user-attachments/assets/c93abddf-be8f-4b45-9b5b-0196e0c338ae" />

2. 키 페어 설정
<img width="198" height="147" alt="image" src="https://github.com/user-attachments/assets/20d30f14-2481-4e86-935f-7efb7e80aed9" />

3. 네트워크 설정
<img width="355" height="465" alt="image" src="https://github.com/user-attachments/assets/aeb218dc-90fd-4262-9efb-f35cff00bad6" />

4. 인바운드 규칙 추가
<img width="1583" height="271" alt="image" src="https://github.com/user-attachments/assets/1d8975bc-594f-4ca9-bd26-00fee27876c1" />

## RDS
### RDS 요약
<img width="1486" height="760" alt="image" src="https://github.com/user-attachments/assets/c121f7dd-fd25-47ea-8a1b-97a6a9321539" />

### RDS 생성
1. 데이터베이스 생성 - MySQL 생성
<img width="1219" height="495" alt="image" src="https://github.com/user-attachments/assets/7ddbb747-73d2-49f3-a94e-a616b4770c49" />

2. 템플릿 및 가용성 선택
<img width="544" height="662" alt="image" src="https://github.com/user-attachments/assets/ef3c1f65-9ada-4156-8145-0a500011a653" />

3. 마스터 사용자 이름 및 암호 설정
<img width="226" height="492" alt="image" src="https://github.com/user-attachments/assets/ac36bbc6-07b3-42d8-8491-25aa016af874" />

4. EC2와 RDS 보안 그룹을 AWS 리소스 연결 (보안 그룹 설정을 대신 해줌)
<img width="1000" height="260" alt="image" src="https://github.com/user-attachments/assets/3f260d26-3fd6-41f2-bf30-24db1a954490" />

5. 서브넷 설정 및 퍼블릭 액세스 설정
<img width="1604" height="694" alt="image" src="https://github.com/user-attachments/assets/bc21ff8c-fda1-4280-8049-d3d25e1ddade" />

## S3

### S3 생성
1. 리전 확인 및 버킷 이름 설정
<img width="240" height="298" alt="image" src="https://github.com/user-attachments/assets/fd00d7ea-de59-4d13-9cf4-ff5c11e89fe5" />

2. 퍼블릭 액세스 차단 설정
<img width="671" height="344" alt="image" src="https://github.com/user-attachments/assets/3ee657a6-88fd-42ad-9fed-e5d4ed98d1de" />

## IAM 생성

### 테스트
1. 사용자 이름 설정
<img width="477" height="131" alt="image" src="https://github.com/user-attachments/assets/9904d5db-6c50-4937-8287-1cea67eaabd5" />

2. 권한 설정
<img width="341" height="258" alt="image" src="https://github.com/user-attachments/assets/42229356-ad86-4966-91d2-8aa4a1f8434b" />

### 실전(?)
1. 권한 작업 추가
<img width="308" height="480" alt="image" src="https://github.com/user-attachments/assets/419d8960-9e73-4e5e-acc8-dd842240cd9e" />

2. 리소스 추가
<img width="577" height="350" alt="image" src="https://github.com/user-attachments/assets/c10631ed-ba45-40fb-876d-519a55490b96" />

3. 완성된 정책
```json
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Sid": "Statement1",
			"Effect": "Allow",
			"Action": [
				"s3:GetObject",
				"s3:PutObject",
				"s3:DeleteObject"
			],
			"Resource": [
				"arn:aws:s3:::spring9-lsj-test/*"
			]
		}
	]
}
```
- Resoucre를  버킷 객체를 지정했지만 더 정확하게 하고 싶으면 폴더 까지 넣을 것
- ex) arn:aws:s3:::spring9-lsj-test/profileimage/*

---
# Health Check API

1. Spring Security 설정
`.requestMatchers("/health").permitAll()` 추가
2. Controller 생성
```java
@Slf4j
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> check(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();

        try {

            InetAddress localHost = InetAddress.getLocalHost();

            String ipAddress = localHost.getHostAddress();

            String ipAddressRemote = request.getRemoteAddr();

            String hostname = localHost.getHostName();

            sb.append("[Health Check] ip : ").append(ipAddress)
                    .append(", remote : ").append(ipAddressRemote)
                    .append(", hostname : ").append(hostname);

            log.info(sb.toString());

        } catch (UnknownHostException e) {

            log.info("Health Check Exception : {}", e.getLocalizedMessage());

            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(sb.toString());
    }
}
```
3. 테스트 사진
<img width="643" height="314" alt="스크린샷 2025-12-23 11 30 55" src="https://github.com/user-attachments/assets/0d37b6a2-0f78-4631-89ba-27616d691222" />

---
# 대용량 데이터 처리

## 1. Projection 활용 (1300ms)
<img width="1315" height="536" alt="image" src="https://github.com/user-attachments/assets/7244010b-3c6e-4725-9771-9df87ae4eeab" />

<img width="1318" height="480" alt="image" src="https://github.com/user-attachments/assets/617b52b2-f81b-4e35-8f9b-534521a3f389" />

## 2. 캐시 활용 (처음 조회 시 : 2000ms, 이후 조회 시 : 10ms)

### a. 처음 조회 시 (2000ms)
<img width="1320" height="468" alt="image" src="https://github.com/user-attachments/assets/f5e31326-0abf-4e11-b88c-24a65bbac5bb" />
<img width="1320" height="468" alt="image" src="https://github.com/user-attachments/assets/36165c54-7c96-4dcd-8d36-96a7f7125083" />


### b. 이후 조회 시 (10ms)
<img width="1318" height="470" alt="image" src="https://github.com/user-attachments/assets/b03cf482-0a4c-4b46-8902-07fb5e032393" />
<img width="1306" height="541" alt="image" src="https://github.com/user-attachments/assets/09897f51-bbf2-4888-a245-253534180f2e" />

## 3. DB 인덱스 생성 (250ms ~ 50ms)
<img width="1309" height="460" alt="image" src="https://github.com/user-attachments/assets/f9ebaf7a-d223-4a97-a85a-642d0085f51e" />
<img width="1313" height="539" alt="image" src="https://github.com/user-attachments/assets/01e40ad7-81fa-4877-8d94-b2eac04d7e65" />
<img width="1314" height="618" alt="image" src="https://github.com/user-attachments/assets/5f8ea496-337c-4c24-860f-3f35f1a0d84c" />

---

# 리펙토링 문서

## https://www.notion.so/2cb555a1c11f800fbb53e1c1fa9436cd?source=copy_link

