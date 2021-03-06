
using System;
using System.Linq;
using System.IO;
using System.Net;
using System.Threading.Tasks;

public class AsyncDownloadWeb2
{
	public static void Main(string[] args)
	{
		var urls = Console.In.ReadToEnd().Split(new string[]{Environment.NewLine}, StringSplitOptions.RemoveEmptyEntries);

		var dir = args[0];

		Func<string, Task> download = async (url) => {
			var wc = new WebClient();
			var uri = new Uri(url);
			var fileName = Path.Combine(dir, Path.GetFileName(url));

			try {
				byte[] buf = await wc.DownloadDataTaskAsync(uri);

				using (var fs = new FileStream(fileName, FileMode.Create))
				{
					await fs.WriteAsync(buf, 0, buf.Length);
				}

				Console.WriteLine("download: {0} => {1}", url, fileName);
			} catch (Exception ex) {
				Console.WriteLine("failed: {0}, {1}", url, ex.Message);
			}
		};

		Task.WaitAll((from url in urls select download(url)).ToArray());
	}
}
