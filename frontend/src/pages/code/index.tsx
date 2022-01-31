import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/mode-csharp';
import 'ace-builds/src-noconflict/theme-github';
import 'ace-builds/webpack-resolver';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { FaCog, FaSave } from 'react-icons/fa';
import TableWrapper from '../../components/table/TableWrapper';
import { Button } from '../../components/UI/button';
import { Error, PlainText } from '../../components/text/text-styles';
import {
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
  const [isSaveDisabled, setIsSaveDisabled] = useState(false);
  const [isExecuteDisabled, setIsExecuteDisabled] = useState(true);
  const [error, setError] = useState('');
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
    if (!isExecuteDisabled) setIsExecuteDisabled(true);
    setCode(newValue);
  };

  const handleSave = () => {
    setIsSaveDisabled(true);

    functionService
      .saveFunction(Number(functionId), code)
      .then((res) => {
        toast.success(res.data?.message);
        setError('');
        setExecutionResult('');
        setIsExecuteDisabled(false);
      })
      .catch((err) => {
        setError(err.response?.data?.message);
        setExecutionResult('');
        setIsSaveDisabled(false);
        setIsExecuteDisabled(true);
      });
  };

  const handleExecute = () => {
    functionService
      .executeFunction(functionData!.language, Number(functionId))
      .then((res) => {
        setExecutionResult(res.data);
        setError('');
      })
      .catch((err) => toast.error(err?.data?.message));
  };

  return (
    <TableWrapper title="Function" description="Write your code">
      <ButtonContainer>
        <Button
          icon={<FaSave />}
          onClick={handleSave}
          disabled={isSaveDisabled}
        >
          Save
        </Button>
        {isFunctionExecutableSynchronously && (
          <Button
            icon={<FaCog />}
            onClick={handleExecute}
            disabled={isExecuteDisabled}
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
            {error ? (
              <Error>{error}</Error>
            ) : (
              <PlainText>
                {executionResult && JSON.stringify(executionResult)}
              </PlainText>
            )}
          </FunctionResultWrapper>
        )}
        <FunctionInfo>
          <div>
            <PlainText>
              <strong>Name:</strong>
            </PlainText>
            <PlainText>
              <strong>Language:</strong>
            </PlainText>
            <PlainText>
              <strong>Shelf:</strong>
            </PlainText>
            <PlainText>
              <strong>Trigger:</strong>
            </PlainText>
          </div>
          <div>
            <PlainText>{functionData?.functionName}</PlainText>
            <PlainText>{functionData?.language}</PlainText>
            <PlainText>{functionData?.shelfName}</PlainText>
            <PlainText>{functionData?.eventName}</PlainText>
          </div>
        </FunctionInfo>
      </FunctionWrapper>
    </TableWrapper>
  );
};

export default Code;
