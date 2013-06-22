package net.awired.core.updater;

import static net.awired.core.updater.Version.V;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;

public class VersionTest {

    @Test
    public void should_create_version_string() throws Exception {
        assertThat(V(1, 2, 3, "SNAPSHOT").toString()).isEqualTo("V1.2.3-SNAPSHOT");
    }

    @Test
    public void should_compare_version() throws Exception {
        Version version = V(1, 2, 3, "SNAPSHOT");

        assertThat(version.getMajor()).isEqualTo(1);
        assertThat(version.getMinor()).isEqualTo(2);
        assertThat(version.getBugfix()).isEqualTo(3);
        assertThat(version.getSuffix()).isEqualTo("SNAPSHOT");
    }

    @Test
    public void should_compare_version_equals() throws Exception {
        Version version1 = V(1, 2, 3, "SNAPSHOT");
        Version version2 = V(1, 2, 3, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isZero();
        assertThat(version1.equals(version2)).isTrue();
    }

    @Test
    public void should_be_lower_version() throws Exception {
        Version version1 = V(1, 2, 2, "SNAPSHOT");
        Version version2 = V(1, 2, 3, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(-1);
    }

    @Test
    public void should_be_greater_version() throws Exception {
        Version version1 = V(1, 2, 3, "SNAPSHOT");
        Version version2 = V(1, 2, 2, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(1);
    }

    @Test
    public void should_be_lower2_version() throws Exception {
        Version version1 = V(1, 1, 4, "SNAPSHOT");
        Version version2 = V(1, 2, 3, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(-9);
    }

    @Test
    public void should_be_greater2_version() throws Exception {
        Version version1 = V(1, 2, 3, "SNAPSHOT");
        Version version2 = V(1, 3, 2, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(-9);
    }

    @Test
    public void should_be_lower3_version() throws Exception {
        Version version1 = V(0, 1, 4, "SNAPSHOT");
        Version version2 = V(1, 2, 3, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(-109);
    }

    @Test
    public void should_be_greater3_version() throws Exception {
        Version version1 = V(1, 2, 3, "SNAPSHOT");
        Version version2 = V(0, 3, 2, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(91);
    }

    @Test
    public void should_be_greater_without_suffix_version() throws Exception {
        Version version1 = V(0, 3, 2);
        Version version2 = V(0, 3, 2, "SNAPSHOT");

        assertThat(version1.compareTo(version2)).isEqualTo(1);
    }

    @Test
    public void should_be_greater_when_count_is_equal() throws Exception {
        Version version1 = V(0, 3, 12);
        Version version2 = V(0, 4, 2);

        assertThat(version1.compareTo(version2)).isEqualTo(-9);
    }

    @Test
    public void should_support_decimal() throws Exception {
        Version version1 = V(0, 3, 2);
        Version version2 = V(0, 3, 12);

        assertThat(version1.compareTo(version2)).isEqualTo(-1);
    }

    @Test
    public void should_be_same_for_diff_suffix() throws Exception {
        Version version1 = V(0, 1, 4, "SNAPSHOT");
        Version version2 = V(0, 1, 4, "TESTING");

        assertThat(version1.compareTo(version2)).isEqualTo(0);
        assertThat(version1.equals(version2)).isFalse();
    }

    @Test
    public void should_build_full_string() throws Exception {
        assertThat(V(0).toFullVersionString()).isEqualTo("0.0.0");
        assertThat(V(1).toFullVersionString()).isEqualTo("1.0.0");
        assertThat(V(1, 2).toFullVersionString()).isEqualTo("1.2.0");
        assertThat(V(1, 2, 3).toFullVersionString()).isEqualTo("1.2.3");
        assertThat(V(1, 0, 3).toFullVersionString()).isEqualTo("1.0.3");
        assertThat(V(0, 0, 0, "SNAPSHOT").toFullVersionString()).isEqualTo("0.0.0-SNAPSHOT");
    }

    @Test
    public void should_parse_version() throws Exception {
        assertThat(Version.parse("1.2.3-SNAPSHOT")).isEqualTo(V(1, 2, 3, "SNAPSHOT"));
        assertThat(Version.parse("11.22.43-SNAPSHOT")).isEqualTo(V(11, 22, 43, "SNAPSHOT"));
        assertThat(Version.parse("1.0.3-SNAPSHOT")).isEqualTo(V(1, 0, 3, "SNAPSHOT"));
        assertThat(Version.parse("1.2-SNAPSHOT")).isEqualTo(V(1, 2, "SNAPSHOT"));
        assertThat(Version.parse("1-SNAPSHOT")).isEqualTo(V(1, "SNAPSHOT"));
        assertThat(Version.parse("1")).isEqualTo(V(1));
        assertThat(Version.parse("V1")).isEqualTo(V(1));
        assertThat(Version.parse("V1.2.3-SNAPSHOT")).isEqualTo(V(1, 2, 3, "SNAPSHOT"));
        assertThat(Version.parse("Version 1.0.3")).isEqualTo(V(1, 0, 3));
    }

}
