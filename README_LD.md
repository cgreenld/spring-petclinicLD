README for How LD is implimented in this Applicaiton and in Spring in General

Step 1: 
Add the Dependency
'''
<dependency>
    <groupId>com.launchdarkly</groupId>
    <artifactId>launchdarkly-java-server-sdk</artifactId>
    <version>7.0.0</version>
</dependency>

and to make the config gathering a bit easier

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

'''

Step 2: 
Configuration Props for the Server Side SDK
'''
# LaunchDarkly Configuration
launchdarkly.sdk-key=${LD_SDK_KEY:your-server-side-sdk-key}
launchdarkly.offline=false
launchdarkly.base-uri=https://sdk.launchdarkly.com
'''

Step 3;
Let's create a class to configure LD

- See  petclinic/system/LDFeatureFlags.java

