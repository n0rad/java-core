package net.awired.core.updater;

import static net.awired.core.updater.Version.V;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;

public class UpdateTest {

    class TestUpdater implements Updater {
        public boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }
    }

    @Test(expected = IllegalStateException.class)
    public void should_have_from_version_lower_then_to_version() throws Exception {
        new Update(V(2), V(1), new TestUpdater()).getUpdater();
    }

    @Test
    public void should_compare_version() throws Exception {
        Update update = new Update(V(1), V(2), new TestUpdater());
        Update update2 = new Update(V(2), V(3), new TestUpdater());

        assertThat(update.compareTo(update2)).isEqualTo(-100);
    }
}
