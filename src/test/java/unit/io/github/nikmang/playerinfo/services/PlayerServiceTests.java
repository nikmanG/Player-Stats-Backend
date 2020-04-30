package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.services.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PlayerServiceTests {

    private PlayerService context;

    @MockBean
    private PlayerRepository playerRepository;


    @Before
    public void setup() {
        context = new PlayerService(playerRepository);
    }

    @Test
    public void whenAddingNewPlayer() {
        //Given
        //When
        context.addPlayer("12345");

        //Then
        verify(playerRepository, times(1)).save(any());
        verify(playerRepository, times(1)).findByUuid(eq("12345"));
    }

    @Test
    public void whenAddingExistingPlayer() {
        //Given
        when(playerRepository.findByUuid(anyString())).thenReturn(new Player());

        //When
        context.addPlayer("12345");

        //Then
        verify(playerRepository, never()).save(any());
        verify(playerRepository, times(1)).findByUuid(eq("12345"));
    }

    @Test
    public void whenGettingPlayer() {
        //Given
        //When
        context.getPlayer(1);

        //Then
        verify(playerRepository, times(1)).findById(eq(1L));
    }
}
