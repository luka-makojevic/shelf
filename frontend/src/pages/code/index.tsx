import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/mode-csharp';
import 'ace-builds/src-noconflict/theme-github';
import 'ace-builds/webpack-resolver';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { FaCog } from 'react-icons/fa';
import TableWrapper from '../../components/table/TableWrapper';
import { Button } from '../../components/UI/button';
import { PlainText } from '../../components/text/text-styles';
import {
  FunctionButtonWrapper,
  FunctionEditorWrapper,
  FunctionInfo,
  FunctionResultWrapper,
  FunctionWrapper,
} from '../../components/layout/layout.styles';
import { ButtonContainer } from '../../components/table/tableWrapper.styles';
import functionService from '../../services/functionService';
import { FunctionData } from '../../interfaces/dataTypes';

const Code = () => {
  const [code, setCode] = useState('');
  const [functionData, setFunctionData] = useState<FunctionData | null>(null);
  const [isSaveDisabled, setIsSaveDisabled] = useState(true);
  const [executionResult, setExecutionResult] = useState('');

  const { functionId } = useParams();

  const isFunctionExecutableSynchronously = functionData?.eventId === 7;

  const getData = () => {
    functionService
      .getFunction(Number(functionId))
      .then((res) => {
        setFunctionData(res.data);
        setCode(res.data.code);
      })
      .catch((err) => toast.error(err.response?.data?.message));
  };

  useEffect(() => {
    getData();
  }, [functionId]);

  const handleEditorValueChange = (newValue: string) => {
    if (isSaveDisabled) setIsSaveDisabled(false);
    setCode(newValue);
  };

  const handleSave = () => {
    setIsSaveDisabled(true);

    functionService
      .saveFunction(Number(functionId), code)
      .then((res) => toast.success(res.data?.message))
      .catch((err) => toast.error(err.response?.data?.message));
  };

  const handleExecute = () => {
    functionService
      .executeFunction(functionData!.language, Number(functionId))
      .then((res) => setExecutionResult(res.data))
      .catch((err) => toast.error(err?.data?.message));
  };

  return (
    <TableWrapper title="Function" description="Write your code">
      <ButtonContainer>
        {isFunctionExecutableSynchronously && (
          <Button
            icon={<FaCog />}
            onClick={handleExecute}
            disabled={!isSaveDisabled}
          >
            Execute
          </Button>
        )}
      </ButtonContainer>
      <FunctionWrapper>
        <FunctionEditorWrapper>
          <AceEditor
            mode={functionData?.language}
            theme="github"
            value={code}
            onChange={handleEditorValueChange}
            name="CODE_EDITOR"
            width="100%"
          />
        </FunctionEditorWrapper>
        {isFunctionExecutableSynchronously && (
          <FunctionResultWrapper>
            <PlainText>
              <strong>Execution result:</strong>
            </PlainText>
            <PlainText>{executionResult}</PlainText>
          </FunctionResultWrapper>
        )}
        <FunctionInfo>
          <PlainText>Name: {functionData?.functionName}</PlainText>
          <PlainText>Language: {functionData?.language}</PlainText>
          <PlainText>Shelf: {functionData?.shelfName}</PlainText>
          <PlainText>Trigger: {functionData?.eventName}</PlainText>
        </FunctionInfo>
      </FunctionWrapper>
      <FunctionButtonWrapper>
        <Button onClick={handleSave} disabled={isSaveDisabled}>
          Save
        </Button>
      </FunctionButtonWrapper>
    </TableWrapper>
  );
};

export default Code;
