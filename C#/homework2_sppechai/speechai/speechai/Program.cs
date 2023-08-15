using System;

using Microsoft.CognitiveServices.Speech;

using System.Threading.Tasks;
using System.IO;

namespace speechai
{
    internal class Program
    {
        static async Task Main(string[] args)
        {
            while (true)
            {
                var config = SpeechConfig.FromSubscription("02c122a7b8b149b5a645ec6b67fa1e18", "eastus");
                var language = "en-US";
                config.OutputFormat = OutputFormat.Detailed;
                config.RequestWordLevelTimestamps();

                using (var recognizer = new SpeechRecognizer(config, language))
                {
                    Console.WriteLine($"Say something in {language} ...");
                    var result = await recognizer.RecognizeOnceAsync().ConfigureAwait(false);
                    if (result.Reason == ResultReason.RecognizedSpeech)
                    {
                        Console.WriteLine($"RECOGNIZED: Text = {result.Text}");

                        string responseText = "You said: " + result.Text;


                        var config2 = SpeechConfig.FromSubscription("02c122a7b8b149b5a645ec6b67fa1e18", "eastus");

                        var voice = responseText;
                        config.SpeechSynthesisVoiceName = voice;

                        using (var synthesizer = new SpeechSynthesizer(config2))
                        {
                                using (var result2 = await synthesizer.SpeakTextAsync(voice))
                                {
                                    if (result2.Reason == ResultReason.SynthesizingAudioCompleted)
                                    {
                                        Console.WriteLine($"Speech synthesized to speaker for text [{voice}]");
                                    }
                                    else if (result2.Reason == ResultReason.Canceled)
                                    {
                                        var cancellation = SpeechSynthesisCancellationDetails.FromResult(result2);
                                        Console.WriteLine($"CANCELED: Reason={cancellation.Reason}");

                                        if (cancellation.Reason == CancellationReason.Error)
                                        {
                                            Console.WriteLine($"CANCELED: ErrorCode={cancellation.ErrorCode}");
                                            Console.WriteLine($"CANCELED: ErrorDetails=[{cancellation.ErrorDetails}]");
                                            Console.WriteLine($"CANCELED: Did you update the subscription info?");
                                        }
                                    }
                                }
                        }

                    }
                    else if (result.Reason == ResultReason.NoMatch)
                    {
                        Console.WriteLine($"NOMATCH: Speech could not be recognized.");
                    }
                }
            }

        }


    }
}