/*
 * Sonarqube anti-pattern and code smell detection plugin
 * Copyright (C) 2017  Giuseppe Aina, Alexandru Stan, Davide Taibi, Gustavs Venters
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package plugin;

import it.unibz.sonarqube.plugin.CodeSmellsAntiPatternsSensor;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.Settings;
import padl.creator.javafile.eclipse.CompleteJavaFileCreator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.exception.ModelDeclarationException;
import padl.kernel.impl.Factory;

public class CodeSmellsAntiPatternsSensorTest {

    @Mock
    SensorContext sensorContext = Mockito.mock(SensorContext.class);

    @Mock
    Settings settings = Mockito.mock(Settings.class);

    @Mock
    Factory factory = Mockito.mock(Factory.class);

    @Mock
    ICodeLevelModel codeLevelModel = Mockito.mock(ICodeLevelModel.class);

    @Test
    public void testExecuteWithExceptions() throws CreationException {
        CodeSmellsAntiPatternsSensor testSensor = new CodeSmellsAntiPatternsSensor();
        Mockito.when(sensorContext.settings()).thenReturn(settings);
        Mockito.when(settings.getString(Mockito.anyString())).thenReturn("");
        testSensor.setFactory(factory);
        Mockito.when(factory.createCodeLevelModel(Mockito.anyString())).thenReturn(codeLevelModel);
        Mockito.when(codeLevelModel.toString()).thenReturn("Test code level model");
        Mockito.doThrow(new ModelDeclarationException("This exception should not be thrown")).when(codeLevelModel).create(Mockito.any(CompleteJavaFileCreator.class));
        testSensor.execute(sensorContext);
    }

}