import org.junit.jupiter.api.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.P3cConstants;
import org.sonar.plugins.PmdP3CRulesDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PmdP3CRulesDefinitionTest {

    @Test
    void test() {
        PmdP3CRulesDefinition definition = new PmdP3CRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository(P3cConstants.P3C_REPOSITORY_KEY);

        assertThat(repository.name()).isEqualTo(P3cConstants.P3C_REPOSITORY_NAME);
        assertThat(repository.language()).isEqualTo(P3cConstants.LANGUAGE_KEY);

        List<RulesDefinition.Rule> rules = repository.rules();
        assertThat(rules).hasSize(48);

        for (RulesDefinition.Rule rule : rules) {
            assertThat(rule.key()).isNotNull();
            assertThat(rule.internalKey()).isNotNull();
            assertThat(rule.name()).isNotNull();
            assertThat(rule.htmlDescription()).isNotNull();
            assertThat(rule.severity()).isNotNull();

            for (RulesDefinition.Param param : rule.params()) {
                assertThat(param.name()).isNotNull();
                assertThat(param.description())
                        .overridingErrorMessage("Description is not set for parameter '" + param.name() + "' of rule '" + rule.key())
                        .isNotNull();
            }
        }
    }
}
