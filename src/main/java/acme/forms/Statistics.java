
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<Double>				data;

	// Methods


	public void add(final double datum) {
		this.data.add(datum);
	}

	public void add(final List<Double> datum) {
		this.data.addAll(datum);
	}

	public double average() {
		return this.data.stream().mapToDouble(x -> x.doubleValue()).average().getAsDouble();
	}

	public double min() {
		return this.data.stream().mapToDouble(x -> x.doubleValue()).min().getAsDouble();
	}

	public double max() {
		return this.data.stream().mapToDouble(x -> x.doubleValue()).max().getAsDouble();
	}

	public double stddev() {
		double mean = this.average();
		int n = this.data.size();

		double sumSquaredDiffs = 0.0;
		for (double value : this.data)
			sumSquaredDiffs += Math.pow(value - mean, 2);

		return Math.sqrt(sumSquaredDiffs / (n - 1));
	}

	public int count() {
		return this.data.size();
	}

}
