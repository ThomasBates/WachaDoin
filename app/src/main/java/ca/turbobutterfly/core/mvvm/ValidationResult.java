package ca.turbobutterfly.core.mvvm;

import java.util.ArrayList;

class ValidationResult implements IValidationResult
{
    private String _errorMessage;
    private Iterable<String> _memberNames;

    public ValidationResult(String errorMessage, Iterable<String> memberNames)
    {
        _errorMessage = errorMessage;
        _memberNames = memberNames;

        if (_memberNames == null)
        {
            _memberNames = new ArrayList<>();
        }
    }

    @Override
    public String ErrorMessage()
    {
        return _errorMessage;
    }

    @Override
    public Iterable<String> MemberNames()
    {
        return _memberNames;
    }
}
