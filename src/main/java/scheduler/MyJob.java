package scheduler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyJob implements Job {

    public static final String discoveryServiceEndpoint="http://localhost:9091/v1/camera/discovery";
    private final Logger log = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       // System.out.println("My Logic");
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:9091/v1/camera/discovery");
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            log.info("CameraDiscoveryJob executed!");
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            log.error("CameraDiscoveryJob : IOException occurred in Job execution : "+e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e){
            log.error("CameraDiscoveryJob : Exception occurred in Job execution : "+e.getMessage());
            log.error("Refiring job");
            JobExecutionException e2 =
                    new JobExecutionException(e);
            e2.refireImmediately();
            throw e2;
        }
    }

}
