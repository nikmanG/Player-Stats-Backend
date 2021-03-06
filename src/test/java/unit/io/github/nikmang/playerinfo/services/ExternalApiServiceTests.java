package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.services.ExternalApiService;
import io.github.nikmang.playerinfo.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ExternalApiServiceTests {

    private ExternalApiService context;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        RestTemplateBuilder templateBuilder = mock(RestTemplateBuilder.class);

        when(templateBuilder.build()).thenReturn(restTemplate);
        context = new ExternalApiService(playerService, templateBuilder);
    }

    @Test
    public void whenRetrievingPlayerName() {
        //Given
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new ExternalApiService.MinecraftNamePacket());

        //When
        context.getPlayerName("abc-123-45678");

        //Then
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

    @Test
    public void whenFirstUrlThrowsException() {
        //Given
        ExternalApiService.InnerPacket innerPacket = new ExternalApiService.InnerPacket();
        innerPacket.setPlayer(new ExternalApiService.InnerInnerPacket());

        ExternalApiService.BackupNamePacket packet = new ExternalApiService.BackupNamePacket();
        packet.setData(innerPacket);

        when(restTemplate.getForObject(matches("https://sessionserver.mojang.com/session/minecraft/profile/abc12345678"), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        when(restTemplate.getForObject(matches("https://playerdb.co/api/player/minecraft/abc-123-45678"), any())).thenReturn(packet);

        //When
        context.getPlayerName("abc-123-45678");

        //Then
        verify(restTemplate, times(2)).getForObject(anyString(), any());
        verify(restTemplate, times(1)).getForObject(matches("https://playerdb.co/api/player/minecraft/abc-123-45678"), any());
    }

    @Test
    public void whenBothUrlsThrowException() {
        //Given
        when(restTemplate.getForObject(any(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        //When
        String result = context.getPlayerName("abc-123-45678");

        //Then
        verify(restTemplate, times(2)).getForObject(anyString(), any());
        assertNull(result);
    }
}