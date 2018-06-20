package ca.turbobutterfly.core.mvvm;

public interface IValidationResult
{
    String ErrorMessage();
    Iterable<String> MemberNames();
}
